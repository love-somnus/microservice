package com.somnus.microservice.provider.uac.web.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: FeignConfiguration
 * @projectName github
 * @description: TODO
 * org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
 * 留意@Conditional(NotReactiveWebApplicationCondition.class)
 * Spring Cloud Gateway是基于WebFlux的，是ReactiveWeb，所以HttpMessageConverters不会自动注入
 * @date 2021/10/31 20:06
 */
@Configuration
public class FeignConfiguration {

    @Bean
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
