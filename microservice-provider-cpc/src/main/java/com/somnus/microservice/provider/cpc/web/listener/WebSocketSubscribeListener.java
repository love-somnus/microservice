package com.somnus.microservice.provider.cpc.web.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Optional;

/**
 * @author kevin.liu
 * @date 2022/9/19 21:34
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private final RabbitTemplate template;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        Optional.ofNullable(accessor).ifPresent(v -> {

            String uid = accessor.getFirstNativeHeader("uid");

            template.convertAndSend("fanout.banner.exchange", "", new Object());

            log.info("websocket subscribe: uid {} session {}", uid, accessor.getSessionId());
        });
    }

}

