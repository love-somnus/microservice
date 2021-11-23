package com.somnus.microservice.lock;

import com.somnus.microservice.lock.entity.LockType;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock
 * @title: LockDelegate
 * @description: TODO
 * @date 2019/6/14 14:51
 */
public interface LockDelegate {

    /**
     *
     * @param invocation
     * @param lockType  锁的类型，包括LOCK(普通锁)，WRITE_LOCK(写锁)，READ_LOCK(读锁)
     * @param key       锁的Key
     * @param leaseTime 持锁时间，持锁超过此时间则自动丢弃锁(单位毫秒)
     * @param waitTime  没有获取到锁时，等待时间(单位毫秒)
     * @param async     是否采用锁的异步执行方式
     * @param fair      是否采用公平锁
     * @return
     * @throws Throwable
     */
    Object invoke(MethodInvocation invocation, LockType lockType, String key, long leaseTime, long waitTime, boolean async, boolean fair) throws Throwable;
}