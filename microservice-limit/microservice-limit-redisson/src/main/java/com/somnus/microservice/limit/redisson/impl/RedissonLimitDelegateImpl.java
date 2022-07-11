package com.somnus.microservice.limit.redisson.impl;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.exception.LimitException;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.impl
 * @title: RedissonLimitDelegateImpl
 * @description: TODO
 * @date 2019/7/10 17:07
 */
public class RedissonLimitDelegateImpl implements LimitDelegate {

    @Autowired
    private LimitExecutor limitExecutor;

    @Override
    public Object invoke(MethodInvocation invocation, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) throws Throwable {
        boolean status = limitExecutor.tryAccess(key, rate, rateInterval, rateIntervalUnit);
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("key=" + key + " is reach max limited access count=" + rate + " within period=" + rateInterval + " " + rateIntervalUnit.name());
        }
    }
}