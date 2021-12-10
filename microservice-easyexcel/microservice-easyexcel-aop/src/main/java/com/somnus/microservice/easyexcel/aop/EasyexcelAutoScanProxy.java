package com.somnus.microservice.easyexcel.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.DefaultAutoScanProxy;
import com.somnus.microservice.autoconfigure.proxy.mode.ProxyMode;
import com.somnus.microservice.autoconfigure.proxy.mode.ScanMode;
import com.somnus.microservice.easyexcel.annotation.RequestExcel;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;

import java.lang.annotation.Annotation;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.easyexcel.aop
 * @title: EasyexcelAutoScanProxy
 * @description: TODO
 * @date 2019/6/14 14:58
 */
public class EasyexcelAutoScanProxy extends DefaultAutoScanProxy {
    private static final long serialVersionUID = 857037966342626931L;

    private String[] commonInterceptorNames;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    public EasyexcelAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[] { "easyexcelInterceptor" };
        }

        return commonInterceptorNames;
    }

    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[] {  RequestExcel.class, ResponseExcel.class };
        }

        return methodAnnotations;
    }
}