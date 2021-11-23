package com.somnus.microservice.lock.redis.impl;

import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.constant.LockConstant;
import com.somnus.microservice.lock.entity.LockType;
import com.somnus.microservice.lock.redis.exception.RedisLockException;

import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.redis.impl
 * @title: RedisLockExecutorImpl
 * @description: TODO
 * @date 2019/6/14 16:35
 */
@Slf4j
public class RedisLockExecutorImpl implements LockExecutor<RLock> {

    @Value("${" + LockConstant.PREFIX + "}")
    private String prefix;

    private boolean lockCached = true;

    @Autowired
    private RedissonHandler redissonHandler;

    private static ThreadLocal<Pair<RLock, String>> threadLocal = new ThreadLocal<>();

    /** 可重入锁可重复使用 */
    private volatile Map<String, RLock> lockMap = new ConcurrentHashMap<>();

    /** 读写锁 */
    private volatile Map<String, RReadWriteLock> readWriteLockMap = new ConcurrentHashMap<>();

    @PreDestroy
    public void destroy() {
        try {
            redissonHandler.close();
        } catch (Exception e) {
            throw new RedisLockException("Close Redisson failed", e);
        }
    }

    @Override
    public boolean tryLock(LockType lockType, String name, String key, long leaseTime, long waitTime, boolean async, boolean fair) {
        if (StringUtils.isEmpty(name)) {
            throw new RedisLockException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new RedisLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryLock(lockType, compositeKey, leaseTime, waitTime, async, fair);
    }

    @Override
    public boolean tryLock(LockType lockType, String compositeKey, long leaseTime, long waitTime, boolean async, boolean fair){
        if (StringUtils.isEmpty(compositeKey)) {
            throw new RedisLockException("Composite key is null or empty");
        }

        if (lockType != LockType.LOCK && fair) {
            throw new RedisLockException("Fair lock of Redis isn't support for " + lockType);
        }

        redissonHandler.validateStartedStatus();

        if (async) {
            return invokeLockAsync(lockType, compositeKey, leaseTime, waitTime, fair);
        } else {
            return invokeLock(lockType, compositeKey, leaseTime, waitTime, fair);
        }
    }

    @Override
    public void lock(LockType lockType, String name, String key, boolean async, boolean fair) {
        if (StringUtils.isEmpty(name)) {
            throw new RedisLockException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new RedisLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        lock(lockType, compositeKey, async, fair);
    }

    @Override
    public void lock(LockType lockType, String compositeKey, boolean async, boolean fair) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new RedisLockException("Composite key is null or empty");
        }

        if (lockType != LockType.LOCK && fair) {
            throw new RedisLockException("Fair lock of Redis isn't support for " + lockType);
        }

        redissonHandler.validateStartedStatus();

        if (async) {
            invokeLockAsync(lockType, compositeKey, fair);
        } else {
            invokeLock(lockType, compositeKey,fair);
        }
    }


    @SneakyThrows(Exception.class)
    public void unlock(RLock lock){
        if (redissonHandler.isStarted()) {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void unlock(){
        if (!redissonHandler.isStarted()){
            return;
        }
        // 当前线程中获取到pair   如果没有获取到锁 没有必要做释放
        Pair<RLock, String> pair = threadLocal.get();
        if (pair == null) {
            return;
        }
        RLock lock = pair.getKey();
        String lockKey = pair.getValue();
        try{
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("释放redis分布式锁异常,lockKey:{},e:", lockKey, e);
        } finally{
            threadLocal.remove();
        }
    }

    @SneakyThrows(Exception.class)
    private boolean invokeLock(LockType lockType, String key, long leaseTime, long waitTime, boolean fair){
        RLock lock = getLock(lockType, key, fair);

        boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);

        return acquired;
    }

    @SneakyThrows(Exception.class)
    private boolean invokeLockAsync(LockType lockType, String key, long leaseTime, long waitTime, boolean fair) {
        RLock lock = getLock(lockType, key, fair);

        boolean acquired = lock.tryLockAsync(waitTime, leaseTime, TimeUnit.MILLISECONDS).get();

        return acquired;
    }

    @SneakyThrows(Exception.class)
    private void invokeLockAsync(LockType lockType, String key, boolean fair){
        RLock lock = getLock(lockType, key, fair);
        lock.lockAsync().get();
    }

    @SneakyThrows(Exception.class)
    private void invokeLock(LockType lockType, String key, boolean fair){
        RLock lock = getLock(lockType, key, fair);
        lock.lock();
    }

    private RLock getLock(LockType lockType, String key, boolean fair) {
        RLock lock;

        if (lockCached) {
            lock = getCachedLock(lockType, key, fair);
        } else {
            lock = getNewLock(lockType, key, fair);
        }

        threadLocal.set(new Pair<>(lock, key));

        return lock;
    }

    private RLock getNewLock(LockType lockType, String key, boolean fair) {
        RedissonClient redisson = redissonHandler.getRedisson();
        switch (lockType) {
            case LOCK:
                if (fair) {
                    return redisson.getFairLock(key);
                } else {
                    return redisson.getLock(key);
                }
            case READ_LOCK:
                return getCachedReadWriteLock(key, fair).readLock();
            // return redisson.getReadWriteLock(key).readLock();
            case WRITE_LOCK:
                return getCachedReadWriteLock(key, fair).writeLock();
            // return redisson.getReadWriteLock(key).writeLock();
        }

        throw new RedisLockException("Invalid Redis lock type for " + lockType);
    }

    private RLock getCachedLock(LockType lockType, String key, boolean fair) {
        String newKey = lockType + "-" + key + "-" + "fair[" + fair + "]";

        RLock lock = lockMap.get(newKey);
        if (lock == null) {
            RLock newLock = getNewLock(lockType, key, fair);
            lock = lockMap.putIfAbsent(newKey, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private RReadWriteLock getCachedReadWriteLock(String key, boolean fair) {
        String newKey = key + "-" + "fair[" + fair + "]";

        RReadWriteLock readWriteLock = readWriteLockMap.get(newKey);
        if (readWriteLock == null) {
            RedissonClient redisson = redissonHandler.getRedisson();
            RReadWriteLock newReadWriteLock = redisson.getReadWriteLock(key);
            readWriteLock = readWriteLockMap.putIfAbsent(newKey, newReadWriteLock);
            if (readWriteLock == null) {
                readWriteLock = newReadWriteLock;
            }
        }

        return readWriteLock;
    }
}