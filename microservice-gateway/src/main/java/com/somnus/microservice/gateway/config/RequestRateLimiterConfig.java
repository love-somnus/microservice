package com.somnus.microservice.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: RequestRateLimiterConfig
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/7 14:24
 */
@Configuration
public class RequestRateLimiterConfig {

    /**
     * 用户限流
     * 使用这种方式限流，请求路径中必须携带userId参数。
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }

    /**
     * 接口限流
     * 获取请求地址的uri作为限流key
     */
    @Bean
    @Primary
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * ip限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        /*return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))*/
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
