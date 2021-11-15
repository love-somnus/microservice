package com.somnus.microservice.gateway.config.repository;

import com.somnus.microservice.gateway.config.manager.TokenAuthenticationManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
/**
 * @author kevin.liu
 * @title: DefaultSecurityContextRepository
 * @projectName gateway
 * @description: 存储认证授权的相关信息
 * @date 2021/11/15 15:22
 */
@Component
public class DefaultSecurityContextRepository implements ServerSecurityContextRepository {

    public final static String TOKEN_HEADER = "Authorization";

    public final static String BEARER = "Bearer ";

    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> headers = request.getHeaders().get(TOKEN_HEADER);
        if (!CollectionUtils.isEmpty(headers)) {
            String authorization = headers.get(0);
            if (StringUtils.isNotEmpty(authorization)) {
                String token = authorization.substring(BEARER.length());
                if (StringUtils.isNotEmpty(token)) {
                    return tokenAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, null)
                    ).map(SecurityContextImpl::new);
                }
            }
        }
        return Mono.empty();
    }
}

