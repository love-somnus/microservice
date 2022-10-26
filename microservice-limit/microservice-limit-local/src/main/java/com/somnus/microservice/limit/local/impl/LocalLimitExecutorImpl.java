package com.somnus.microservice.limit.local.impl;

import com.somnus.microservice.autoconfigure.proxy.util.Objects;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.local.impl
 * @title: LocalLimitExecutorImpl
 * @description: TODO
 * @date 2019/7/10 16:53
 */
@Slf4j
public class LocalLimitExecutorImpl implements LimitExecutor {

    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":true}")
    private Boolean frequentLogPrint;

    private final Map<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();
    private final Map<String, AtomicBoolean> statusMap = new ConcurrentHashMap<>();
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();
    private final Map<String, Timer> timerMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryAccess(String name, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (Objects.isEmpty(name)) {
            throw new LimitException("Name is null or empty");
        }

        if (Objects.isEmpty(key)) {
            throw new LimitException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryAccess(compositeKey, rate, rateInterval, rateIntervalUnit);
    }

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (Objects.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        Lock lock = getLock(compositeKey);

        try {
            lock.lock();

            AtomicInteger counter = getCounter(compositeKey);
            AtomicBoolean status = getStatus(compositeKey);
            Timer timer = getTimer(compositeKey);

            if (!status.get()) {
                startTimer(counter, status, timer, rateInterval);
                while (!status.get()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            int count = counter.get();
            if (count <= rate) {
                count = counter.incrementAndGet();
            }

            if (frequentLogPrint) {
                log.info("Access try count is {} for key={}", count, compositeKey);
            }

            return count <= rate;
        } finally {
            lock.unlock();
        }
    }

    private void startTimer(AtomicInteger counter, AtomicBoolean status, Timer timer, int limitPeriod) {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                counter.getAndSet(0);
                status.getAndSet(true);
            }
        }, 0L, limitPeriod * 1000L);
    }

    private AtomicInteger getCounter(String compositeKey) {
        AtomicInteger counter = counterMap.get(compositeKey);
        if (counter == null) {
            AtomicInteger newCounter = new AtomicInteger(0);
            counter = counterMap.putIfAbsent(compositeKey, newCounter);
            if (counter == null) {
                counter = newCounter;
            }
        }

        return counter;
    }

    private AtomicBoolean getStatus(String compositeKey) {
        AtomicBoolean status = statusMap.get(compositeKey);
        if (status == null) {
            AtomicBoolean newStatus = new AtomicBoolean(false);
            status = statusMap.putIfAbsent(compositeKey, newStatus);
            if (status == null) {
                status = newStatus;
            }
        }

        return status;
    }

    private Lock getLock(String compositeKey) {
        Lock lock = lockMap.get(compositeKey);
        if (lock == null) {
            Lock newLock = new ReentrantLock();
            lock = lockMap.putIfAbsent(compositeKey, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }

        return lock;
    }

    private Timer getTimer(String compositeKey) {
        Timer timer = timerMap.get(compositeKey);
        if (timer == null) {
            Timer newTimer = new Timer();
            timer = timerMap.putIfAbsent(compositeKey, newTimer);
            if (timer == null) {
                timer = newTimer;
            }
        }

        return timer;
    }
}