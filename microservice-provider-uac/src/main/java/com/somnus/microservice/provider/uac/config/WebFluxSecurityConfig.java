package com.somnus.microservice.provider.uac.config;

import com.somnus.microservice.provider.uac.config.handler.DefaultAccessDeniedHandler;
import com.somnus.microservice.provider.uac.config.handler.DefaultAuthenticationEntryPoint;
import com.somnus.microservice.provider.uac.config.handler.DefaultAuthenticationFailureHandler;
import com.somnus.microservice.provider.uac.config.handler.DefaultAuthenticationSuccessHandler;
import com.somnus.microservice.provider.uac.config.manager.TokenAuthorizationManager;
import com.somnus.microservice.provider.uac.config.manager.TokenAuthenticationManager;
import com.somnus.microservice.provider.uac.config.repository.DefaultSecurityContextRepository;
import com.somnus.microservice.provider.uac.config.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.LinkedList;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.uac.config
 * @title: WebFluxSecurityConfig
 * @description: uac security核心配置类
 * @date 2021/11/11 19:09
 */
@RefreshScope
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfig {

    private final String[] urls = {"/auth/**", "/swagger-ui.html","/webjars/**","/v3/api-docs/**"};

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final TokenAuthenticationManager tokenAuthenticationManager;

    private final TokenAuthorizationManager tokenAuthorizationManager;

    private final DefaultSecurityContextRepository defaultSecurityContextRepository;

    private final DefaultAccessDeniedHandler defaultAccessDeniedHandler;

    private final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    private final DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    private final DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                /* 登录认证处理 */
                .authenticationManager(reactiveAuthenticationManager())
                .securityContextRepository(defaultSecurityContextRepository)
                /* 请求拦截处理 */
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(urls).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // 鉴权处理
                        .anyExchange().access(tokenAuthorizationManager)
                )
                .formLogin()// 自定义处理
                    .authenticationSuccessHandler(defaultAuthenticationSuccessHandler)
                    .authenticationFailureHandler(defaultAuthenticationFailureHandler)
                .and()
                // 鉴权异常处理
                .exceptionHandling()
                    //处理未认证
                    .authenticationEntryPoint(defaultAuthenticationEntryPoint)
                    // 处理未授权
                    .accessDeniedHandler(defaultAccessDeniedHandler)
                .and()
                .csrf().disable()
        ;
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
     * 注册用户信息验证管理器，可按需求添加多个按顺序执行
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        managers.add(authentication -> {
            // 其他登陆方式 (比如手机号验证码登陆) 可在此设置不得抛出异常或者 Mono.error
            return Mono.empty();
        });
        // 必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此 AuthenticationManager 会调用 Mono.error 造成后面的 AuthenticationManager 不生效
        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceImpl));
        managers.add(tokenAuthenticationManager);
        return new DelegatingReactiveAuthenticationManager(managers);
    }
}
