package com.somnus.microservice.lock.configuration;

import com.somnus.microservice.lock.aop.LockAutoScanProxy;
import com.somnus.microservice.lock.aop.LockInterceptor;
import com.somnus.microservice.lock.constant.LockConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.configuration
 * @title: LockAopConfiguration
 * @description: TODO
 * @date 2019/6/14 14:57
 */
@Configuration
public class LockAopConfiguration {

    @Value("${" + LockConstant.LOCK_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    public LockAutoScanProxy lockAutoScanProxy() {
        return new LockAutoScanProxy(scanPackages);
    }

    @Bean
    public LockInterceptor lockInterceptor() {
        return new LockInterceptor();
    }
}
