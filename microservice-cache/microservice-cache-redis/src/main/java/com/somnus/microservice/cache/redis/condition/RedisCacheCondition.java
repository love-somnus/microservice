package com.somnus.microservice.cache.redis.condition;

import com.somnus.microservice.cache.condition.CacheCondition;
import com.somnus.microservice.cache.constant.CacheConstant;

/**
 * @author Kevin
 * @date 2019/7/5 15:57
 */
public class RedisCacheCondition extends CacheCondition {
    public RedisCacheCondition() {
        super(CacheConstant.CACHE_TYPE, CacheConstant.CACHE_TYPE_REDIS);
    }
}