package com.somnus.microservice.cache.redis.impl;

import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.cache.constant.CacheConstant;
import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.exception.BusinessException;
import com.somnus.microservice.commons.base.utils.PublicUtil;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.somnus.microservice.cache.CacheDelegate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache.redis.impl
 * @title: RedisCacheDelegateImpl
 * @description: TODO
 * @date 2019/7/5 16:00
 */
@Slf4j
public class RedisCacheDelegateImpl implements CacheDelegate {

    @Autowired
    private RedisHandler redisHandler;

    @Autowired
    private RedissonHandler redissonHandler;

    @Value("${" + CacheConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + CacheConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    @Value("${" + CacheConstant.CACHE_AOP_EXCEPTION_IGNORE + ":true}")
    private Boolean cacheAopExceptionIgnore;

    @Override
    public Object invokeCacheable(MethodInvocation invocation, String key, long expire) throws Throwable {
        RBloomFilter<Object> bloomFilter = redissonHandler.getRedisson().getBloomFilter(CacheConstant.BLOOM_FLITER);
        bloomFilter.tryInit(100000000, 0.001);

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        /* 解决缓存穿透(存入过过滤器的值，一定能判定出存在或者不存在；未存入过过滤器的值，可能会被误判存入过), */
        if(!bloomFilter.contains(key)){
            throw new BusinessException(ErrorCodeEnum.EN10027);
        }

        Object object = null;
        try {
            object = valueOperations.get(key);
            if (frequentLogPrint) {
                log.info("Before invocation, Cacheable key={}, cache={} in Redis", key, object);
            }
        } catch (Exception e) {
            if (cacheAopExceptionIgnore) {
                log.error("Redis exception occurs while Cacheable", e);
            } else {
                throw e;
            }
        }

        if (object != null) {
            /* ***********************因为序列化去掉了类信息，反序列化的时候只能把map转换下**********************  */
            Class<?> clazz = invocation.getMethod().getReturnType();
            if(List.class.isAssignableFrom(clazz)){
                return object;
            }
            Map<String, Object> map = (Map<String, Object>)object;
            return PublicUtil.mapToBean(map, clazz);
            /* ***********************end**********************  */
        }

        object = invocation.proceed();

        if (object != null) {
            try {
                //倾向使用不过期的key，可以解决缓存击穿
                if (expire == -1) {
                    valueOperations.set(key, object);
                } else {
                    valueOperations.set(key, object, expire, TimeUnit.MILLISECONDS);
                }
                if (frequentLogPrint) {
                    log.info("After invocation, Cacheable key={}, cache={} in Redis", key, object);
                }
            } catch (Exception e) {
                if (cacheAopExceptionIgnore) {
                    log.error("Redis exception occurs while Cacheable", e);
                } else {
                    throw e;
                }
            }
        }
        return object;
    }

    @Override
    public Object invokeCachePut(MethodInvocation invocation, String key, long expire) throws Throwable {
        RBloomFilter<Object> bloomFilter = redissonHandler.getRedisson().getBloomFilter(CacheConstant.BLOOM_FLITER);
        bloomFilter.tryInit(100000000, 0.001);

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        // 空值不缓存
        Object object = invocation.proceed();
        if (object != null) {
            try {
                if (expire == -1) {
                    /* 存入布隆过滤器 */
                    log.info("key={}, bloomFilter={} in RBloomFilter", key, CacheConstant.BLOOM_FLITER);
                    bloomFilter.add(key);
                    /* 存入缓存 */
                    valueOperations.set(key, object);
                } else {
                    valueOperations.set(key, object, expire, TimeUnit.MILLISECONDS);
                }
                if (frequentLogPrint) {
                    log.info("After invocation, CachePut key={}, cache={} in Redis", key, object);
                }
            } catch (Exception e) {
                if (cacheAopExceptionIgnore) {
                    log.error("Redis exception occurs while CachePut", e);
                } else {
                    throw e;
                }
            }
        }

        return object;
    }

    @Override
    public Object invokeCacheEvict(MethodInvocation invocation, String key, String name, boolean allEntries, boolean beforeInvocation) throws Throwable {
        if (beforeInvocation) {
            try {
                clear(key, name, allEntries);
                if (frequentLogPrint) {
                    if (allEntries) {
                        log.info("Before invocation, CacheEvict clear all keys with prefix={} in Redis", key);
                    } else {
                        log.info("Before invocation, CacheEvict clear key={} in Redis", key);
                    }
                }
            } catch (Exception e) {
                if (cacheAopExceptionIgnore) {
                    log.error("Redis exception occurs while CacheEvict", e);
                } else {
                    throw e;
                }
            }
        }

        Object object = invocation.proceed();

        if (!beforeInvocation) {
            try {
                clear(key, name, allEntries);
                if (frequentLogPrint) {
                    if (allEntries) {
                        log.info("After invocation, CacheEvict clear all keys with prefix={} in Redis", key);
                    } else {
                        log.info("After invocation, CacheEvict clear key={} in Redis", key);
                    }
                }
            } catch (Exception e) {
                if (cacheAopExceptionIgnore) {
                    log.error("Redis exception occurs while CacheEvict", e);
                } else {
                    throw e;
                }
            }
        }

        return object;
    }

    private void clear(String key, String name, boolean allEntries) {
        String compositeWildcardKey;
        if (allEntries) {
            compositeWildcardKey = KeyUtil.getCompositeWildcardKey(prefix, name);
        } else {
            compositeWildcardKey = KeyUtil.getCompositeWildcardKey(key);
        }

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();
        Set<String> keys = redisTemplate.keys(compositeWildcardKey);
        Optional.ofNullable(keys).ifPresent(set -> set.forEach(k -> redisTemplate.delete(k)));
    }
}