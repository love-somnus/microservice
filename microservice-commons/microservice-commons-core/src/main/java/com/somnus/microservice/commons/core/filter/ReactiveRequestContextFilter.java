package com.somnus.microservice.commons.core.filter;

import com.somnus.microservice.commons.base.holder.ReactiveRequestContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: ReactiveRequestContextFilter
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/26 15:18
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        ReactiveRequestContextHolder.put(request);

        return chain.filter(exchange)
                .doFinally(s -> ReactiveRequestContextHolder.reset());

        /*return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(ReactiveRequestContextHolder.CONTEXT_KEY, request));*/
    }
}
