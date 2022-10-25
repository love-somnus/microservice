package com.somnus.microservice.provider.cpc.web.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.exception.BusinessException;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.commons.core.support.Optionally;
import com.somnus.microservice.commons.core.support.SpringContextHolder;
import com.somnus.microservice.provider.cpc.model.response.UserProfileResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.io.IOException;
import java.util.Objects;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:42
 */
@Slf4j
public class WsChannelInterceptor implements ChannelInterceptor {

    /**
     * 在消息发送之前执行, 如果此方法返回值为空, 则不会发生实际的消息发送
     * @param message 消息
     * @param channel 通道
     * @return 消息
     */
    @Override
    @SneakyThrows(IOException.class)
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        Environment env = SpringContextHolder.getBean(Environment.class);

        /*StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);*/
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        /* 判断是否首次连接请求, 如果已经连接, 返回message */
        if (StompCommand.CONNECT.equals(Objects.requireNonNull(accessor).getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            log.info( "authorization:[{}]>>>>>>>>>>开始连接服务器。。。。。。", authorization);

            /* 鉴权（必须登录用户才能使用websocket服务）*/
            String url = env.getProperty("wt.config.server-url");

            String result = Request.Get(url).setHeader("Authorization", authorization).execute().returnContent().asString();

            Wrapper<UserProfileResponse> wrapper = JacksonUtil.parseJson(result, new TypeReference<Wrapper<UserProfileResponse>>(){});

            Optionally.ofNullable(wrapper).trueThrow(v -> wrapper.error(), () -> new BusinessException(ErrorCodeEnum.EN10019));

            // 设置当前访问器的认证用户
            accessor.setUser(()-> wrapper.getResult().getUid());

            accessor.addNativeHeader("uid", wrapper.getResult().getUid());

            return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("preSend: {} close connect", JacksonUtil.toJson(accessor));
        }
        return message;
    }

    /**
     * 消息发送之后进行调用,是否有异常,进行数据清理
     * @param message 消息
     * @param channel 通道
     * @param sent
     * @param ex
     */
    @Override
    public void afterSendCompletion(@NonNull Message<?> message, @NonNull MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
        /* 判断是否首次连接请求, 如果已经连接, 返回message */
        if (StompCommand.ACK.equals(Objects.requireNonNull(accessor).getCommand())) {
            String id = accessor.getFirstNativeHeader("id");
            String messageId = accessor.getFirstNativeHeader("message-id");
            log.info("消息id-->[{}]-->message-id-->[{}]已被接收消费", id, messageId);
        }
    }
}
