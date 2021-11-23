package com.somnus.microservice.limit.local.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.local.impl
 * @title: GuavaLocalLimitExecutorImpl
 * @description: TODO
 * @date 2019/7/10 16:51
 */
public class GuavaLocalLimitExecutorImpl implements LimitExecutor {
    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":true}")
    private Boolean frequentLogPrint;

    private volatile Map<String, RateLimiterEntity> rateLimiterEntityMap = new ConcurrentHashMap<String, RateLimiterEntity>();

    @Override
    public boolean tryAccess(String name, String key, int limitPeriod, int limitCount) {
        if (StringUtils.isEmpty(name)) {
            throw new LimitException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LimitException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryAccess(compositeKey, limitPeriod, limitCount);
    }

    @Override
    public boolean tryAccess(String compositeKey, int limitPeriod, int limitCount) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        if (limitPeriod != 1) {
            throw new LimitException("Limit period must be 1 second for Guava rate limiter");
        }

        RateLimiterEntity rateLimiterEntity = getRateLimiterEntity(compositeKey, limitCount);
        RateLimiter rateLimiter = rateLimiterEntity.getRateLimiter();

        return rateLimiter.tryAcquire();
    }

    private RateLimiterEntity getRateLimiterEntity(String compositeKey, double rate) {
        RateLimiterEntity rateLimiterEntity = rateLimiterEntityMap.get(compositeKey);
        if (rateLimiterEntity == null) {
            RateLimiter newRateLimiter = RateLimiter.create(rate);

            RateLimiterEntity newRateLimiterEntity = new RateLimiterEntity();
            newRateLimiterEntity.setRateLimiter(newRateLimiter);
            newRateLimiterEntity.setRate(rate);

            rateLimiterEntity = rateLimiterEntityMap.putIfAbsent(compositeKey, newRateLimiterEntity);
            if (rateLimiterEntity == null) {
                rateLimiterEntity = newRateLimiterEntity;
            }
        } else {
            if (rateLimiterEntity.getRate() != rate) {
                rateLimiterEntity.getRateLimiter().setRate(rate);
                rateLimiterEntity.setRate(rate);
            }
        }

        return rateLimiterEntity;
    }

    // 因为 rateLimiter.setRate(permitsPerSecond)会执行一次synchronized，为避免不必要的同步，故通过RateLimiterEntity去封装，做一定的冗余设计
    private class RateLimiterEntity {
        private RateLimiter rateLimiter;
        private double rate;

        public RateLimiter getRateLimiter() {
            return rateLimiter;
        }

        public void setRateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }
}