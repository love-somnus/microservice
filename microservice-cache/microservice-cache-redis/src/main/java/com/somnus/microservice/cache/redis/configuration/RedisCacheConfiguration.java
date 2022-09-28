package com.somnus.microservice.cache.redis.configuration;

import com.somnus.microservice.cache.CacheDelegate;
import com.somnus.microservice.cache.redis.condition.RedisCacheCondition;
import com.somnus.microservice.cache.redis.impl.RedisCacheDelegateImpl;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.commons.redis.handler.RedisHandlerImpl;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Kevin
 * @date 2019/7/5 15:59
 */
@Configuration
public class RedisCacheConfiguration {

    @Bean
    @Conditional(RedisCacheCondition.class)
    public CacheDelegate redisCacheDelegate(RedisHandler redisHandler, RedissonHandler redissonHandler) {
        return new RedisCacheDelegateImpl(redisHandler, redissonHandler);
    }

    @Bean
    @Conditional(RedisCacheCondition.class)
    @ConditionalOnMissingBean(RedisHandler.class)
    public RedisHandler redisHandler(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisHandlerImpl(redisTemplate, stringRedisTemplate);
    }
}