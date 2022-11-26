package com.somnus.microservice.commons.redis.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Map;

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

    <T> T opsForHashValue(Class<T> clazz, String key, Object hashKey);

    Map<Object, Object> opsForHashMap(String key);

    void opsPutHashValue(String key, Object hashKey, Object value);

    void opsPutHashValue(String key, Object hashKey, Object value, Duration duration);

    <T> T opsForValue(Class<T> clazz, String key);

    void opsSetValue(String key, Object value);

    void opsSetValue(String key, Object value, Duration duration);

    /*  ******************************stringRedisTemplate****************************************** */

    String opsForStringHashValue(String key, String hashKey);

    Map<String, String> opsForStringHashMap(String key);

    void opsPutStringHashValue(String key, Object hashKey, String value);

    void opsPutStringHashValue(String key, Object hashKey, String value, Duration duration);

    String opsForStringValue(String key);

    void opsSetStringValue(String key, String value);

    void opsSetStringValue(String key, String value, Duration duration);
}