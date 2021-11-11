package com.somnus.microservice.commons.dubbo.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.AbstractConfig;
import org.apache.dubbo.config.spring.beans.factory.annotation.DubboFeignBuilder;
import feign.Feign;
import org.apache.dubbo.config.spring.beans.factory.annotation.ServiceClassPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.util.Set;

import static org.apache.dubbo.spring.boot.util.DubboUtils.DUBBO_PREFIX;
import static org.apache.dubbo.spring.boot.util.DubboUtils.DUBBO_SCAN_PREFIX;
import static org.apache.dubbo.spring.boot.util.DubboUtils.BASE_PACKAGES_PROPERTY_NAME;
import static java.util.Collections.emptySet;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.dubbo.core
 * @title: DubboAutoConfiguration
 * @description: TODO
 * @date 2021/3/18 11:22
 */
@Slf4j
@Configuration
@AutoConfigureOrder
@ConditionalOnClass(AbstractConfig.class)
@ConditionalOnProperty(prefix = DUBBO_PREFIX, name = "enabled", matchIfMissing = true, havingValue = "true")
public class DubboAutoConfiguration {

    @Bean
    @Primary
    public Feign.Builder dubboFeignBuilder() {
        return new DubboFeignBuilder();
    }

    @Bean
    @ConditionalOnClass(ConfigurationPropertySources.class)
    @ConditionalOnProperty(prefix = DUBBO_SCAN_PREFIX, name = BASE_PACKAGES_PROPERTY_NAME)
    public ServiceClassPostProcessor serviceClassPostProcessor(Environment environment) {
        Set<String> packagesToScan = environment.getProperty("dubbo.scan.base-packages", Set.class, emptySet());
        return new ServiceClassPostProcessor(packagesToScan);
    }

}