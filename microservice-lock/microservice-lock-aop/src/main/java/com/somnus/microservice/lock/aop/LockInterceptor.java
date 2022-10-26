package com.somnus.microservice.lock.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.somnus.microservice.autoconfigure.proxy.aop.AbstractInterceptor;
import com.somnus.microservice.autoconfigure.proxy.util.Objects;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.annotation.Lock;
import com.somnus.microservice.lock.annotation.ReadLock;
import com.somnus.microservice.lock.annotation.WriteLock;
import com.somnus.microservice.lock.constant.LockConstant;
import com.somnus.microservice.lock.entity.LockType;
import com.somnus.microservice.lock.exception.LockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.aop
 * @title: LockInterceptor
 * @description: TODO
 * @date 2019/6/14 15:03
 */
@Slf4j
@RequiredArgsConstructor
public class LockInterceptor extends AbstractInterceptor {

    private final LockDelegate lockDelegate;

    @Value("${" + LockConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LockConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        Lock lockAnnotation = getLockAnnotation(invocation);
        if (lockAnnotation != null) {
            String name = lockAnnotation.name();
            String key = lockAnnotation.key();
            long leaseTime = lockAnnotation.leaseTime();
            long waitTime = lockAnnotation.waitTime();
            boolean async = lockAnnotation.async();
            boolean fair = lockAnnotation.fair();

            return invoke(invocation, lockAnnotation, name, key, leaseTime, waitTime, async, fair);
        }

        ReadLock readLockAnnotation = getReadLockAnnotation(invocation);
        if (readLockAnnotation != null) {
            String name = readLockAnnotation.name();
            String key = readLockAnnotation.key();
            long leaseTime = readLockAnnotation.leaseTime();
            long waitTime = readLockAnnotation.waitTime();
            boolean async = readLockAnnotation.async();
            boolean fair = readLockAnnotation.fair();

            return invoke(invocation, readLockAnnotation, name, key, leaseTime, waitTime, async, fair);
        }

        WriteLock writeLockAnnotation = getWriteLockAnnotation(invocation);
        if (writeLockAnnotation != null) {
            String name = writeLockAnnotation.name();
            String key = writeLockAnnotation.key();
            long leaseTime = writeLockAnnotation.leaseTime();
            long waitTime = writeLockAnnotation.waitTime();
            boolean async = writeLockAnnotation.async();
            boolean fair = writeLockAnnotation.fair();

            return invoke(invocation, writeLockAnnotation, name, key, leaseTime, waitTime, async, fair);
        }

        return invocation.proceed();
    }

    private Lock getLockAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Lock.class)) {
            return method.getAnnotation(Lock.class);
        }

        return null;
    }

    private ReadLock getReadLockAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(ReadLock.class)) {
            return method.getAnnotation(ReadLock.class);
        }

        return null;
    }

    private WriteLock getWriteLockAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(WriteLock.class)) {
            return method.getAnnotation(WriteLock.class);
        }

        return null;
    }

    private Object invoke(MethodInvocation invocation, Annotation annotation, String name, String key, long leaseTime, long waitTime, boolean async, boolean fair) throws Throwable {
        LockType lockType = getLockType(annotation);
        if (lockType == null) {
            throw new LockException("Lock type is null for " + annotation);
        }

        String lockTypeValue = lockType.getValue();

        if (Objects.isEmpty(name)) {
            throw new LockException("Annotation [" + lockTypeValue + "]'s name is null or empty");
        }

        if (Objects.isEmpty(key)) {
            throw new LockException("Annotation [" + lockTypeValue + "]'s key is null or empty");
        }

        String spelKey = getSpelKey(invocation, key);
        String compositeKey = KeyUtil.getCompositeKey(prefix, name, spelKey);
        String proxyType = getProxyType(invocation);
        String proxiedClassName = getProxiedClassName(invocation);
        String methodName = getMethodName(invocation);

        if (frequentLogPrint) {
            log.info("Intercepted for annotation - {} [key={}, leaseTime={}, waitTime={}, async={}, fair={}, proxyType={}, proxiedClass={}, method={}]", lockTypeValue, compositeKey, leaseTime, waitTime, async, fair, proxyType, proxiedClassName, methodName);
        }

        return lockDelegate.invoke(invocation, lockType, compositeKey, leaseTime, waitTime, async, fair);
    }

    private LockType getLockType(Annotation annotation) {
        if (annotation instanceof Lock) {
            return LockType.LOCK;
        } else if (annotation instanceof ReadLock) {
            return LockType.READ_LOCK;
        } else if (annotation instanceof WriteLock) {
            return LockType.WRITE_LOCK;
        }

        return null;
    }
}