package com.somnus.microservice.limit.local.impl;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.local.impl
 * @title: LocalLimitDelegateImpl
 * @description: TODO
 * @date 2019/7/10 16:53
 */
@RequiredArgsConstructor
public class LocalLimitDelegateImpl implements LimitDelegate {

    private final LimitExecutor limitExecutor;

    @Override
    public Object invoke(MethodInvocation invocation, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) throws Throwable {
        boolean status = limitExecutor.tryAccess(key, rate, rateInterval, rateIntervalUnit);
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("Reach max limited access rate=" + rate + " within rateInterval=" + rateInterval + " " + rateIntervalUnit.name());
        }
    }
}