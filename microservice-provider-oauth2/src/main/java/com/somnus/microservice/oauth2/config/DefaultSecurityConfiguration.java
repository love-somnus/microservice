package com.somnus.microservice.oauth2.config;

import com.somnus.microservice.commons.security.core.FormIdentityLoginConfigurer;
import com.somnus.microservice.commons.security.core.UserDetailsAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author kevin.liu
 * @title: DefaultSecurityConfiguration
 * @projectName oauth2
 * @description: TODO
 * @date 2021/11/16 14:06
 */
@EnableWebSecurity
public class DefaultSecurityConfiguration {

    /**
     * spring security 默认的安全策略
     * @param http security注入点
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .build();
        /*return http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .headers().frameOptions().sameOrigin()// 避免iframe同源无法登录
                .and().apply(new FormIdentityLoginConfigurer()) // 表单登录个性化
                .and()
                .build();*/
    }

    /**
     * BCrypt密码编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder()
                .username("admin")
                .password("password")
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }*/

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/auth/**", "/swagger-ui/**","/swagger-resources/**","/v2/api-docs","/v3/api-docs", "/actuator/**", "/css/**", "/error");
    }

}
