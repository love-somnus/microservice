package com.somnus.microservice.limit.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.DefaultAutoScanProxy;
import com.somnus.microservice.autoconfigure.proxy.mode.ProxyMode;
import com.somnus.microservice.autoconfigure.proxy.mode.ScanMode;
import com.somnus.microservice.limit.annotation.Limit;

import java.lang.annotation.Annotation;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.aop
 * @title: LimitAutoScanProxy
 * @description: TODO
 * @date 2019/7/10 16:36
 */
public class LimitAutoScanProxy extends DefaultAutoScanProxy {

    private static final long serialVersionUID = -6456216398492047529L;

    private String[] commonInterceptorNames;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    public LimitAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[] { "limitInterceptor" };
        }

        return commonInterceptorNames;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[] { Limit.class };
        }

        return methodAnnotations;
    }
}