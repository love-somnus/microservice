package com.somnus.microservice.commons.redis.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redis.handler
 * @title: RedisHandler
 * @description: TODO
 * @date 2019/7/5 16:43
 */
public interface RedisHandler {
    /**
     * 获取RedisTemplate
     * @return
     */
    RedisTemplate<String, Object> getRedisTemplate();

    /**
     * 获取StringRedisTemplate
     * @return
     */
    StringRedisTemplate getStringRedisTemplate();
}