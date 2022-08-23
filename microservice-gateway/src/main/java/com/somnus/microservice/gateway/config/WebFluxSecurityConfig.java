package com.somnus.microservice.gateway.config;

import com.somnus.microservice.gateway.config.converter.JwtTokenAuthenticationConverter;
import com.somnus.microservice.gateway.config.handler.DefaultAuthenticationEntryPoint;
import com.somnus.microservice.gateway.config.handler.DefaultServerAccessDeniedHandler;
import com.somnus.microservice.gateway.config.manager.TokenAuthenticationManager;
import com.somnus.microservice.gateway.config.manager.TokenAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.gateway.config
 * @title: WebFluxSecurityConfig
 * @description: webflux security核心配置类
 * @date 2021/11/11 19:09
 */
@RefreshScope
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfig {

    private static final String[] URLS = {"/oauth2/**", "/uac/v3/api-docs/**", "/cpc/v3/api-docs/**", "/swagger-ui.html","/webjars/**","/v3/api-docs/**"};

    private final RedisTemplate<String, Object> redisTemplate;

    /*private final TokenAuthenticationManager tokenAuthenticationManager;*/

    /*private final TokenAuthorizationManager tokenAuthorizationManager;*/

    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    private final DefaultServerAccessDeniedHandler defaultServerAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        // JWT处理
        httpSecurity.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
        // 自定义处理JWT请求头过期或签名错误的结果                      // 自定义处理JWT请求头鉴权失败的结果
        httpSecurity.oauth2ResourceServer().authenticationEntryPoint(defaultAuthenticationEntryPoint).accessDeniedHandler(defaultServerAccessDeniedHandler);
        /* 请求拦截处理 */
        httpSecurity.authorizeExchange(exchange -> {
            exchange.pathMatchers(HttpMethod.OPTIONS).permitAll().pathMatchers(URLS).permitAll().anyExchange().authenticated();
        }).csrf().disable();
        return httpSecurity.build();
    }

    /**
     * BCrypt密码编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
     * 需要把jwt的Claim中的authorities加入
     * 方案：重新定义ReactiveAuthenticationManager权限管理器[JwtReactiveAuthenticationManager]
     * 默认转换器JwtGrantedAuthoritiesConverter
     *
     * org.springframework.security.oauth2.core.OAuth2TokenValidator
     * org.springframework.security.oauth2.jwt.JwtTimestampValidator
     *
     * org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
     */
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        /*JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);*/
        JwtTokenAuthenticationConverter jwtAuthenticationConverter = new JwtTokenAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setRedisTemplate(redisTemplate);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
