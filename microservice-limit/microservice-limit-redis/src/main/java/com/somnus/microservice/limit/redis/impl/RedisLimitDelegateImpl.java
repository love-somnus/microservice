package com.somnus.microservice.limit.redis.impl;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.exception.LimitException;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.impl
 * @title: RedisLimitDelegateImpl
 * @description: TODO
 * @date 2019/7/10 17:07
 */
public class RedisLimitDelegateImpl implements LimitDelegate {

    @Autowired
    private LimitExecutor limitExecutor;

    @Override
    public Object invoke(MethodInvocation invocation, String key, int limitPeriod, int limitCount) throws Throwable {
        boolean status = limitExecutor.tryAccess(key, limitPeriod, limitCount);
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("key=" + key + " is reach max limited access count=" + limitCount + " within period=" + limitPeriod + " seconds");
        }
    }
}