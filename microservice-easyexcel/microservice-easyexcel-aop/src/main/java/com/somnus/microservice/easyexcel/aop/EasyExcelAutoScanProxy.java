package com.somnus.microservice.easyexcel.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.DefaultAutoScanProxy;
import com.somnus.microservice.autoconfigure.proxy.mode.ProxyMode;
import com.somnus.microservice.autoconfigure.proxy.mode.ScanMode;
import com.somnus.microservice.easyexcel.annotation.RequestExcel;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;

import java.lang.annotation.Annotation;

/**
 * @author Kevin
 * @date 2019/6/14 14:58
 */
public class EasyExcelAutoScanProxy extends DefaultAutoScanProxy {
    private static final long serialVersionUID = 857037966342626931L;

    private String[] commonInterceptorNames;

    private Class<? extends Annotation>[] methodAnnotations;

    public EasyExcelAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[] { "easyExcelInterceptor" };
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