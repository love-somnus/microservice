package com.somnus.microservice.autoconfigure.proxy.mode;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.proxy.mode
 * @title: ScanMode
 * @description: TODO
 * @date 2019/6/14 9:56
 */
public enum ScanMode {
    /**
     * 只执行扫描到接口名或者类名上的注解后的处理
     */
    FOR_CLASS_ANNOTATION_ONLY,

    /**
     * 只执行扫描到接口或者类方法上的注解后的处理
     */
    FOR_METHOD_ANNOTATION_ONLY,

    /**
     * 上述两者都执行
     */
    FOR_CLASS_OR_METHOD_ANNOTATION
}