package com.somnus.microservice.commons.redis.handler;

import com.somnus.microservice.commons.base.utils.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2019/7/5 16:51
 */
@RequiredArgsConstructor
public class RedisHandlerImpl implements RedisHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 获取RedisTemplate
     * @return RedisTemplate
     */
    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取StringRedisTemplate
     * @return StringRedisTemplate
     */
    @Override
    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    @Override
    public <T> T opsForHashValue(Class<T> clazz, String key, Object hashKey) {
        return clazz.cast(redisTemplate.opsForHash().get(key, hashKey));
    }

    @Override
    public Map<Object, Object> opsForHashMap(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return Optional.of(map).orElse(Collections.emptyMap());
    }

    @Override
    public void opsPutHashValue(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void opsPutHashValue(String key, Object hashKey, Object value, Duration duration) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, duration);
    }

    @Override
    public <T> T opsForValue(Class<T> clazz, String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if(Objects.isEmpty(value)){
            return null;
        }
        return clazz.cast(value);
    }

    @Override
    public void opsSetValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void opsSetValue(String key, Object value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    /*  ******************************stringRedisTemplate****************************************** */
    @Override
    public String opsForStringHashValue(String key, String hashKey) {
        return (String)stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<String, String> opsForStringHashMap(String key) {
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        if(Objects.isEmpty(map)){
            return Collections.emptyMap();
        }
        return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
    }

    @Override
    public void opsPutStringHashValue(String key, Object hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void opsPutStringHashValue(String key, Object hashKey, String value, Duration duration) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        stringRedisTemplate.expire(key, duration);
    }

    @Override
    public String opsForStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void opsSetStringValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void opsSetStringValue(String key, String value, Duration duration) {
        stringRedisTemplate.opsForValue().set(key, value, duration);
    }
}