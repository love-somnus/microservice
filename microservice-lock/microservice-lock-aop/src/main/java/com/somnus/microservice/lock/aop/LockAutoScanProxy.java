package com.somnus.microservice.lock.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.DefaultAutoScanProxy;
import com.somnus.microservice.autoconfigure.proxy.mode.ProxyMode;
import com.somnus.microservice.autoconfigure.proxy.mode.ScanMode;
import com.somnus.microservice.lock.annotation.Lock;
import com.somnus.microservice.lock.annotation.ReadLock;
import com.somnus.microservice.lock.annotation.WriteLock;

import java.lang.annotation.Annotation;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.aop
 * @title: LockAutoScanProxy
 * @description: TODO
 * @date 2019/6/14 14:58
 */
public class LockAutoScanProxy extends DefaultAutoScanProxy {
    private static final long serialVersionUID = -957037966342626931L;

    private String[] commonInterceptorNames;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    public LockAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[] { "lockInterceptor" };
        }

        return commonInterceptorNames;
    }

    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[] { Lock.class, ReadLock.class, WriteLock.class };
        }

        return methodAnnotations;
    }
}