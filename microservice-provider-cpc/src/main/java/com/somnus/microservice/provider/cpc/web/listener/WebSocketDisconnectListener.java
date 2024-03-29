package com.somnus.microservice.provider.cpc.web.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

/**
 * @author kevin.liu
 * @date 2022/9/19 15:19
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        Optional.ofNullable(accessor).ifPresent(v -> {

            String uid = accessor.getFirstNativeHeader("uid");

            log.info("websocket offline: uid {} session {}", uid, accessor.getSessionId());
        });
    }
}

