package com.somnus.microservice.gateway.web.filter;

import com.somnus.microservice.commons.base.utils.JwksUtil;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: GlobalAuthenticationFilter
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/24 9:43
 */
@Component
public class GlobalAuthenticationFilter  implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(token)){
            return chain.filter(exchange);
        }
        String accessToken = StringUtils.substringAfter(token, OAuth2AccessToken.TokenType.BEARER.getValue()).trim();
        Claims claims = JwksUtil.parseJwtRsa256(accessToken);
        Map<String, Object> map = claims.get("user_info", Map.class);
        Map<String, String> filterMap = map.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, entry -> encode(entry.getValue().toString())));
        /*ServerHttpRequest request = exchange.getRequest().mutate().headers(header -> map.forEach((key, value) -> header.set(key, value))).build();*/
        ServerHttpRequest request = exchange.getRequest().mutate().headers(header -> filterMap.forEach(header::set)).build();
        ServerWebExchange build = exchange.mutate().request(request).build();
        return chain.filter(build);
    }

    @SneakyThrows
    private String encode(String value){
        return java.net.URLEncoder.encode(value, "UTF-8");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
