package com.somnus.microservice.limit;

import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit
 * @title: LimitDelegate
 * @description: TODO
 * @date 2019/7/10 16:27
 */
public interface LimitDelegate {

    Object invoke(MethodInvocation invocation, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) throws Throwable;

}