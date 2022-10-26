package com.somnus.microservice.provider.cpc.web.listener;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.provider.cpc.api.exception.CpcBizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import com.somnus.microservice.provider.cpc.web.interceptor.WsChannelInterceptor.WsPrincipal;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author kevin.liu
 * @date 2022/9/19 21:34
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    /*private final ConfigProperties properties;*/

    private final RabbitTemplate template;

    /*private final ProfileService service;*/

    /*private final FarmService farmService;*/

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        Optional.ofNullable(accessor).ifPresent(v -> {

            String id = accessor.getFirstNativeHeader("id");

            WsPrincipal principal = Optional.ofNullable((WsPrincipal)accessor.getUser()).orElseThrow(() -> new CpcBizException(ErrorCodeEnum.EN10000));

            try{
                TimeUnit.MILLISECONDS.sleep(100L);
            }catch (Exception e){
                e.printStackTrace();
            }

            if("play.song".equals(id)){

                /*service.pushPlaylist(properties.getActiveSeason().getSeason(), principal.getResponse().getUid());*/
            }

            if("notify".equals(id)){
                /*farmService.pushWsLand(properties.getActiveSeason().getSeason(), principal.getResponse().getUid());*/
            }

            if("banner".equals(id)){

                /*template.convertAndSend("fanout.banner.exchange", "", properties.getActiveSeason());*/
            }

            log.info("websocket id>>>>[{}] subscribe: uid {} session {}", id, principal.getResponse().getUid(), accessor.getSessionId());
        });
    }

}

