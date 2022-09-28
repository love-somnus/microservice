package com.somnus.microservice.provider.cpc.autoconfigure;

import com.somnus.microservice.provider.cpc.web.interceptor.HttpHandshakeInterceptor;
import com.somnus.microservice.provider.cpc.web.interceptor.WsChannelInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:49
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private final WsChannelInterceptor wsChannelInterceptor;

    private final HttpHandshakeInterceptor httpHandshakeInterceptor;

    /**
     * 链接站点配置
     * @param registry 注册器
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*
         * 注册 Stomp的端点
         *
         * addEndpoint：添加STOMP协议的端点。这个HTTP URL是供WebSocket或SockJS客户端访问的地址
         * withSockJS： 指定端点使用SockJS协议
         */
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .addInterceptors(httpHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /* registry.enableSimpleBroker("/exchange","/topic","/queue","/amq/queue");*/

        /* 服务端通知特定用户客户端的前缀，可以不设置，默认为user */
        registry.setUserDestinationPrefix("/user");

        /* 配置客户端发送请求消息的一个或多个前缀，该前缀会筛选消息目标转发到Controller 类中注解对应的方法里*/
        registry.setApplicationDestinationPrefixes("/ws");

        /* 配置消息代理 使用RabbitMQ做为消息代理，替换默认的Simple Broker */
        registry.enableStompBrokerRelay("/exchange","/topic","/queue")
                .setVirtualHost(virtualHost)
                .setRelayHost(host)
                .setRelayPort(61613)
                .setClientLogin(username)
                .setClientPasscode(password)
                .setSystemLogin(username)
                .setSystemPasscode(password)
                .setSystemHeartbeatSendInterval(5000)
                .setSystemHeartbeatReceiveInterval(4000)
        ;
        log.info("init rabbitmq websocket MessageBroker completed....");
    }

    /**
     * 设置输入通道的线程数, 默认线程数为1, 可以自定义线程数, 最大线程数, 线程存活时间等
     * @param registration 注册器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        /*
         * 配置消息线程池
         * 1、corePoolSize() 配置核心线程池, 当线程数小于此配置时, 不管线程中有无空闲的线程, 都会产生新线程处理任务
         * 2、maxPoolSize() 配置线程池最大数, 当线程池等于此配置时, 不会产生新线程
         * keepAliveSeconds() 线程池维护线程所允许的空闲时间, 单位为秒
         */
        registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);

        log.info("Configure client inbound channel started ...");

        /*
         * 添加STOMP自定义拦截器
         * 消息拦截器, 实现ChannelInterceptor接口
         */
        registration.interceptors(wsChannelInterceptor);

        log.info("Configure client inbound channel completed ...");
    }

    /**
     * 设置输出通道的线程数, 默认线程数为1, 可以自定义线程数, 最大线程数, 线程存活时间等
     * @param registration 注册器
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);

        log.info("Configure client outbound channel started ...");

        /*
         * 添加STOMP自定义拦截器
         * 消息拦截器, 实现ChannelInterceptor接口
         */
        registration.interceptors(wsChannelInterceptor);

        log.info("Configure client outbound channel completed ...");
    }

}
