package com.somnus.microservice.lock.zookeeper.impl;

import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.constant.LockConstant;
import com.somnus.microservice.lock.entity.LockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.zookeeper.impl
 * @title: ZookeeperLockDelegateImpl
 * @description: TODO
 * @date 2019/7/30 14:46
 */
@Slf4j
@RequiredArgsConstructor
public class ZookeeperLockDelegateImpl implements LockDelegate {

    private final LockExecutor<InterProcessMutex> lockExecutor;

    @Value("${" + LockConstant.LOCK_AOP_EXCEPTION_IGNORE + ":true}")
    private Boolean lockAopExceptionIgnore;

    @Override
    public Object invoke(MethodInvocation invocation, LockType lockType, String key, long leaseTime, long waitTime, boolean async, boolean fair) throws Throwable {
        try {
            boolean lock  = lockExecutor.tryLock(lockType, key, leaseTime, waitTime, async, fair);
            if (lock) {
                return invocation.proceed();
            }
        } catch (Exception e) {
            if (lockAopExceptionIgnore) {
                log.error("Zookeeper exception occurs while Lock", e);
                return invocation.proceed();
            } else {
                throw e;
            }
        } finally {
            lockExecutor.unlock();
        }

        return invocation.proceed();
    }
}