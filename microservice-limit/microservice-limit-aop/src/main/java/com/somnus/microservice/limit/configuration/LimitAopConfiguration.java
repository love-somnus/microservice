package com.somnus.microservice.limit.configuration;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.aop.LimitAutoScanProxy;
import com.somnus.microservice.limit.aop.LimitInterceptor;
import com.somnus.microservice.limit.constant.LimitConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.configuration
 * @title: LimitAopConfiguration
 * @description: TODO
 * @date 2019/7/10 16:44
 */
@ConditionalOnProperty(prefix = "limit",value = "enabled",havingValue = "true")
public class LimitAopConfiguration {

    @Value("${" + LimitConstant.LIMIT_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    public LimitAutoScanProxy limitAutoScanProxy() {
        return new LimitAutoScanProxy(scanPackages);
    }

    @Bean
    @ConditionalOnBean(LimitDelegate.class)
    public LimitInterceptor limitInterceptor(LimitDelegate delegate) {
        return new LimitInterceptor(delegate);
    }
}
