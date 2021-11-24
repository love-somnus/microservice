package com.somnus.microservice.cache.redis.configuration;

import com.somnus.microservice.cache.CacheDelegate;
import com.somnus.microservice.cache.redis.condition.RedisCacheCondition;
import com.somnus.microservice.cache.redis.impl.RedisCacheDelegateImpl;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.commons.redis.handler.RedisHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache.redis.configuration
 * @title: RedisCacheConfiguration
 * @description: TODO
 * @date 2019/7/5 15:59
 */
@Configuration
public class RedisCacheConfiguration {
    @Bean
    @Conditional(RedisCacheCondition.class)
    public CacheDelegate redisCacheDelegate() {
        return new RedisCacheDelegateImpl();
    }

    @Bean
    @Conditional(RedisCacheCondition.class)
    public RedisHandler redisHandler() {
        return new RedisHandlerImpl();
    }
}