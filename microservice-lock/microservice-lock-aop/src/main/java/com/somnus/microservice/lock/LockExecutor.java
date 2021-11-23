package com.somnus.microservice.lock;

import com.somnus.microservice.lock.entity.LockType;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock
 * @title: LockExecutor
 * @description: TODO
 * @date 2019/6/14 14:51
 */
public interface LockExecutor<T> {
    /**
     * 尝试获取锁，如果获取到锁，则返回锁对象，如果未获取到锁，则返回空
     * @param lockType 锁的类型，包括LOCK(普通锁)，WRITE_LOCK(写锁)，READ_LOCK(读锁)
     * @param name 锁的名字
     * @param key 锁的Key
     * @param leaseTime 持锁时间，持锁超过此时间则自动丢弃锁(单位毫秒)
     * @param waitTime 没有获取到锁时，等待时间(单位毫秒)
     * @param async 是否采用锁的异步执行方式
     * @param fair 是否采用公平锁
     */
    boolean tryLock(LockType lockType, String name, String key, long leaseTime, long waitTime, boolean async, boolean fair);

    boolean tryLock(LockType lockType, String compositeKey, long leaseTime, long waitTime, boolean async, boolean fair);

    /**
     * 主动获取锁，没抢到就等待，直到拿到锁
     * @param lockType 锁的类型，包括LOCK(普通锁)，WRITE_LOCK(写锁)，READ_LOCK(读锁)
     * @param name 锁的名字
     * @param key 锁的Key
     * @param async 是否采用锁的异步执行方式
     * @param fair 是否采用公平锁
     */
    void lock(LockType lockType, String name, String key, boolean async, boolean fair);

    void lock(LockType lockType, String compositeKey, boolean async, boolean fair);

    void unlock(T t) throws Exception;

    void unlock();
}