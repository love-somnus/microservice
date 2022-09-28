package com.somnus.microservice.limit.redis.impl;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.impl
 * @title: RedisLimitDelegateImpl
 * @description: TODO
 * @date 2019/7/10 17:07
 */
@RequiredArgsConstructor
public class RedisLimitDelegateImpl implements LimitDelegate {

    private final LimitExecutor limitExecutor;

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