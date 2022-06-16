package com.somnus.microservice.commons.security.core;

import com.somnus.microservice.commons.security.handler.FormAuthenticationFailureHandler;
import com.somnus.microservice.commons.security.handler.SsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * <p>基于授权码模式 统一认证登录 spring security & sas 都可以使用 所以抽取成 HttpConfigurer</p>
 * @author kevin.liu
 * @title: FormIdentityLoginConfigurer
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 13:15
 */
public final class FormIdentityLoginConfigurer
        extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> {
            formLogin.loginPage("/token/login");
            formLogin.loginProcessingUrl("/token/form");
            formLogin.failureHandler(new FormAuthenticationFailureHandler());

        }).logout() // SSO登出成功处理
                .logoutSuccessHandler(new SsoLogoutSuccessHandler()).deleteCookies("JSESSIONID")
                .invalidateHttpSession(true).and().csrf().disable();
    }

}
