package com.somnus.microservice.gateway.config.manager;

import cn.hutool.core.convert.Convert;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author kevin.liu
 * @title: TokenAuthorizationManager
 * @description: 用户权限鉴权处理
 *               ReactiveAuthorizationManager用于基于URL的鉴权
 * @date 2021/11/15 15:27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisHandler redisHandler;

    private static final String REQUEST_KEY = "wt:config:security:path";

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        URI uri = authorizationContext.getExchange().getRequest().getURI();

        Object value = redisHandler.getRedisTemplate().opsForHash().get(REQUEST_KEY, uri.getPath());

        List<String> authorities = Convert.toList(String.class, value);

        return authentication.filter(Authentication::isAuthenticated)//判断是否认证成功
                .flatMapIterable(Authentication::getAuthorities)//获取认证后的全部权限
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)//如果权限包含则判断为true
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}