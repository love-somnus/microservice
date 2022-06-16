package com.somnus.microservice.oauth2.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * @author kevin.liu
 * @title: SecurityMessageSourceConfiguration
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 16:47
 */
@ConditionalOnWebApplication(type = SERVLET)
public class SecurityMessageSourceConfiguration implements WebMvcConfigurer {

    @Bean
    public MessageSource securityMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/errors/messages");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }

}