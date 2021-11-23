package com.somnus.microservice.autoconfigure.proxy.mode;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.proxy.mode
 * @title: ProxyMode
 * @description: TODO
 * @date 2019/6/14 9:56
 */
public enum ProxyMode {
    // 只通过扫描到接口名或者类名上的注解后，来确定是否要代理
    BY_CLASS_ANNOTATION_ONLY,

    // 只通过扫描到接口或者类方法上的注解后，来确定是否要代理
    BY_METHOD_ANNOTATION_ONLY,

    // 上述两者都可以
    BY_CLASS_OR_METHOD_ANNOTATION
}