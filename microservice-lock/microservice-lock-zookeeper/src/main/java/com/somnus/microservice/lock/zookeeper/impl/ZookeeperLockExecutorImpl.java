package com.somnus.microservice.lock.zookeeper.impl;

import com.somnus.microservice.autoconfigure.proxy.util.Objects;
import com.somnus.microservice.autoconfigure.proxy.util.Pair;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.base.constant.GlobalConstant;
import com.somnus.microservice.commons.zookeeper.handler.CuratorHandler;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.entity.LockType;
import com.somnus.microservice.lock.zookeeper.exception.ZookeeperLockException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.zookeeper.impl
 * @title: ZookeeperLockExecutorImpl
 * @description: TODO
 * @date 2019/7/30 14:44
 */
@Slf4j
@RequiredArgsConstructor
public class ZookeeperLockExecutorImpl implements LockExecutor<InterProcessMutex> {

    @Value("${" + GlobalConstant.PREFIX + "}")
    private String prefix;

    private boolean lockCached = true;

    private final CuratorHandler curatorHandler;

    private static final ThreadLocal<Pair<String, InterProcessMutex>> threadLocal = new ThreadLocal<>();

    /** 可重入锁可重复使用 */
    private final Map<String, InterProcessMutex> lockMap = new ConcurrentHashMap<String, InterProcessMutex>();

    /** 读写锁 */
    private final Map<String, InterProcessReadWriteLock> readWriteLockMap = new ConcurrentHashMap<>();

    @PreDestroy
    public void destroy() {
        try {
            curatorHandler.close();
        } catch (Exception e) {
            throw new ZookeeperLockException("Close Curator failed", e);
        }
    }

    @Override
    public boolean tryLock(LockType lockType, String name, String key, long leaseTime, long waitTime, boolean async, boolean fair){
        if (Objects.isEmpty(name)) {
            throw new ZookeeperLockException("Name is null or empty");
        }

        if (Objects.isEmpty(key)) {
            throw new ZookeeperLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryLock(lockType, compositeKey, leaseTime, waitTime, async, fair);
    }

    @Override
    @SneakyThrows(Exception.class)
    public boolean tryLock(LockType lockType, String compositeKey, long leaseTime, long waitTime, boolean async, boolean fair) {
        if (Objects.isEmpty(compositeKey)) {
            throw new ZookeeperLockException("Composite key is null or empty");
        }

        if (fair) {
            throw new ZookeeperLockException("Fair lock of Zookeeper isn't  support  for " + lockType);
        }

        if (async) {
            throw new ZookeeperLockException("Async lock of Zookeeper isn't  support for " + lockType);
        }

        curatorHandler.validateStartedStatus();

        InterProcessMutex interProcessMutex = getLock(lockType, compositeKey);

        return interProcessMutex.acquire(waitTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void lock(LockType lockType, String name, String key, boolean async, boolean fair) {
        if (Objects.isEmpty(name)) {
            throw new ZookeeperLockException("Name is null or empty");
        }

        if (Objects.isEmpty(key)) {
            throw new ZookeeperLockException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        lock(lockType, compositeKey, async, fair);
    }

    @Override
    @SneakyThrows(Exception.class)
    public void lock(LockType lockType, String compositeKey, boolean async, boolean fair) {
        if (Objects.isEmpty(compositeKey)) {
            throw new ZookeeperLockException("Composite key is null or empty");
        }

        if (fair) {
            throw new ZookeeperLockException("Fair lock of Zookeeper isn't support for " + lockType);
        }

        if (async) {
            throw new ZookeeperLockException("Async lock of Zookeeper isn't support for " + lockType);
        }

        curatorHandler.validateStartedStatus();

        InterProcessMutex interProcessMutex = getLock(lockType, compositeKey);

        interProcessMutex.acquire();
    }

    @Override
    @SneakyThrows(Exception.class)
    public void unlock(InterProcessMutex interProcessMutex){
        if (curatorHandler.isStarted()) {
            if (interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()) {
                interProcessMutex.release();
            }
        }
    }

    @Override
    public void unlock() {
        if (!curatorHandler.isStarted()){
            return;
        }
        // 当前线程中获取到pair   如果没有获取到锁 没有必要做释放
        Pair<String, InterProcessMutex> pair = threadLocal.get();
        if (pair == null) {
            return;
        }
        String lockKey = pair.getKey();
        InterProcessMutex lock = pair.getValue();
        try{
            if (lock != null && lock.isAcquiredInThisProcess()) {
                lock.release();
            }
        } catch (Exception e) {
            log.error("释放zookeeper分布式锁异常,lockKey:{},e:", lockKey, e);
        } finally{
            threadLocal.remove();
        }
    }

    private InterProcessMutex getLock(LockType lockType, String key) {
        InterProcessMutex lock;

        if (lockCached) {
            lock = getCachedLock(lockType, key);
        } else {
            lock = getNewLock(lockType, key);
        }

        threadLocal.set(new Pair<>(key, lock));

        return lock;
    }

    private InterProcessMutex getNewLock(LockType lockType, String key) {
        String path = curatorHandler.getPath(prefix, key);
        CuratorFramework curator = curatorHandler.getCurator();
        switch (lockType) {
            case LOCK:
                return new InterProcessMutex(curator, path);
            case READ_LOCK:
                return getCachedReadWriteLock(key).readLock();
            // return new InterProcessReadWriteLock(curator, path).readLock();
            case WRITE_LOCK:
                return getCachedReadWriteLock(key).writeLock();
            // return new InterProcessReadWriteLock(curator, path).writeLock();
        }

        throw new ZookeeperLockException("Invalid Zookeeper lock type for " + lockType);
    }

    private InterProcessMutex getCachedLock(LockType lockType, String key) {
        String path = curatorHandler.getPath(prefix, key);

        String newKey = path + "-" + lockType;

        InterProcessMutex lock = lockMap.get(newKey);
        if (lock == null) {
            InterProcessMutex newLock = getNewLock(lockType, key);
            lock = lockMap.putIfAbsent(newKey, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private InterProcessReadWriteLock getCachedReadWriteLock(String key) {
        String path = curatorHandler.getPath(prefix, key);

        String newKey = path;

        InterProcessReadWriteLock readWriteLock = readWriteLockMap.get(newKey);

        if (readWriteLock == null) {
            CuratorFramework curator = curatorHandler.getCurator();
            InterProcessReadWriteLock newReadWriteLock = new InterProcessReadWriteLock(curator, path);
            readWriteLock = readWriteLockMap.putIfAbsent(newKey, newReadWriteLock);
            if (readWriteLock == null) {
                readWriteLock = newReadWriteLock;
            }
        }

        return readWriteLock;
    }
}