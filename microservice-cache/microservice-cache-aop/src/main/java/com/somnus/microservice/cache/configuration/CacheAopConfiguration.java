package com.somnus.microservice.cache.configuration;

import com.somnus.microservice.cache.CacheDelegate;
import com.somnus.microservice.cache.aop.CacheAutoScanProxy;
import com.somnus.microservice.cache.aop.CacheInterceptor;
import com.somnus.microservice.cache.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author Kevin
 * @date 2019/7/5 15:39
 */
@ConditionalOnProperty(prefix = "cache",value = "enabled",havingValue = "true")
public class CacheAopConfiguration {

    @Value("${" + CacheConstant.CACHE_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    public CacheAutoScanProxy cacheAutoScanProxy() {
        return new CacheAutoScanProxy(scanPackages);
    }

    @Bean
    @ConditionalOnBean(CacheDelegate.class)
    public CacheInterceptor cacheInterceptor(CacheDelegate delegate) {
        return new CacheInterceptor(delegate);
    }
}