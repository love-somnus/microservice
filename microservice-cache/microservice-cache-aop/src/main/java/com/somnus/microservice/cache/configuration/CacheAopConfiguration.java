package com.somnus.microservice.cache.configuration;

import com.somnus.microservice.cache.CacheDelegate;
import com.somnus.microservice.cache.aop.CacheAutoScanProxy;
import com.somnus.microservice.cache.aop.CacheInterceptor;
import com.somnus.microservice.cache.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @date 2019/7/5 15:39
 */
@Configuration
public class CacheAopConfiguration {

    @Value("${" + CacheConstant.CACHE_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    public CacheAutoScanProxy cacheAutoScanProxy() {
        return new CacheAutoScanProxy(scanPackages);
    }

    @Bean
    public CacheInterceptor cacheInterceptor(CacheDelegate delegate) {
        return new CacheInterceptor(delegate);
    }
}