package com.somnus.microservice.lock.local.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.somnus.microservice.autoconfigure.proxy.util.Pair;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.constant.LockConstant;
import com.somnus.microservice.lock.entity.LockType;
import com.somnus.microservice.lock.local.exception.LocalLockException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.local.impl
 * @title: LocalLockExecutorImpl
 * @description: TODO
 * @date 2019/6/14 18:06
 */
@Slf4j
@RequiredArgsConstructor
public class LocalLockExecutorImpl implements LockExecutor<Lock> {

    @Value("${" + LockConstant.PREFIX + "}")
    private String prefix;

    private boolean lockCached = true;

    private static final ThreadLocal<Pair<String, Lock>> threadLocal = new ThreadLocal<>();

    /** 可重入锁可重复使用 */
    private volatile Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    /** 读写锁 */
    private final Map<String, ReadWriteLock> readWriteLockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryLock(LockType lockType, String name, String key, long leaseTime, long waitTime, boolean async, boolean fair){
        if (StringUtils.isEmpty(name)) {
            throw new LocalLockException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LocalLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryLock(lockType, compositeKey, leaseTime, waitTime, async, fair);
    }

    @Override
    @SneakyThrows(Exception.class)
    public boolean tryLock(LockType lockType, String compositeKey, long leaseTime, long waitTime, boolean async, boolean fair){
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LocalLockException("Composite key is null or empty");
        }

        if (async) {
            throw new LocalLockException("Async lock of Local isn't support for " + lockType);
        }

        Lock lock = getLock(lockType, compositeKey, fair);

        boolean acquired = lock.tryLock(waitTime, TimeUnit.MILLISECONDS);

        return acquired;
    }

    @Override
    public void lock(LockType lockType, String name, String key, boolean async, boolean fair) {
        if (StringUtils.isEmpty(name)) {
            throw new LocalLockException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LocalLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        lock(lockType, compositeKey, async, fair);
    }

    @Override
    public void lock(LockType lockType, String compositeKey, boolean async, boolean fair) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LocalLockException("Composite key is null or empty");
        }

        if (async) {
            throw new LocalLockException("Async lock of Local isn't support for " + lockType);
        }

        Lock lock = getLock(lockType, compositeKey, fair);

        lock.lock();
    }

    @Override
    @SneakyThrows(Exception.class)
    public void unlock(Lock lock){
        if (lock != null) {
            if (lock instanceof ReentrantLock) {
                ReentrantLock reentrantLock = (ReentrantLock) lock;
                // 只有ReentrantLock提供isLocked方法
                if (reentrantLock.isLocked()) {
                    reentrantLock.unlock();
                }
            } else {
                lock.unlock();
            }
        }
    }

    @Override
    public void unlock(){
        // 当前线程中获取到pair   如果没有获取到锁 没有必要做释放
        Pair<String, Lock> pair = threadLocal.get();
        if (pair == null) {
            return;
        }
        String lockKey = pair.getKey();
        Lock lock = pair.getValue();
        try{
            if (lock != null) {
                if (lock instanceof ReentrantLock) {
                    ReentrantLock reentrantLock = (ReentrantLock) lock;
                    // 只有ReentrantLock提供isLocked方法
                    if (reentrantLock.isLocked()) {
                        reentrantLock.unlock();
                    }
                } else {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            log.error("释放local锁异常,lockKey:{},e:", lockKey, e);
        } finally{
            threadLocal.remove();
        }
    }

    private Lock getLock(LockType lockType, String key, boolean fair) {
        Lock lock ;
        if (lockCached) {
            lock = getCachedLock(lockType, key, fair);
        } else {
            lock = getNewLock(lockType, key, fair);
        }

        threadLocal.set(new Pair<>(key, lock));

        return lock;
    }

    private Lock getNewLock(LockType lockType, String key, boolean fair) {
        switch (lockType) {
            case LOCK:
                return new ReentrantLock(fair);
            case READ_LOCK:
                return getCachedReadWriteLock(key, fair).readLock();
            case WRITE_LOCK:
                return getCachedReadWriteLock(key, fair).writeLock();
        }

        throw new LocalLockException("Invalid Local lock type for " + lockType);
    }

    private Lock getCachedLock(LockType lockType, String key, boolean fair) {
        String newKey = lockType + "-" + key + "-" + "fair[" + fair + "]";

        Lock lock = lockMap.get(newKey);
        if (lock == null) {
            Lock newLock = getNewLock(lockType, key, fair);
            lock = lockMap.putIfAbsent(newKey, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private ReadWriteLock getCachedReadWriteLock(String key, boolean fair) {
        String newKey = key + "-" + "fair[" + fair + "]";

        ReadWriteLock readWriteLock = readWriteLockMap.get(newKey);
        if (readWriteLock == null) {
            ReadWriteLock newReadWriteLock = new ReentrantReadWriteLock(fair);
            readWriteLock = readWriteLockMap.putIfAbsent(newKey, newReadWriteLock);
            if (readWriteLock == null) {
                readWriteLock = newReadWriteLock;
            }
        }

        return readWriteLock;
    }
}