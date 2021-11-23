package com.somnus.microservice.lock.redis.impl;

import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.entity.LockType;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.redis.impl
 * @title: RedisLockDelegateImpl
 * @description: TODO
 * @date 2019/6/14 16:35
 */
@Slf4j
public class RedisLockDelegateImpl implements LockDelegate {

    @Autowired
    private LockExecutor<RLock> lockExecutor;

    @Override
    public Object invoke(MethodInvocation invocation, LockType lockType, String key, long leaseTime, long waitTime, boolean async, boolean fair) throws Throwable {
        try {
            boolean lock  = lockExecutor.tryLock(lockType, key, leaseTime, waitTime, async, fair);
            if (lock) {
                return invocation.proceed();
            }
        } finally {
            lockExecutor.unlock();
        }

        return null;
    }
}