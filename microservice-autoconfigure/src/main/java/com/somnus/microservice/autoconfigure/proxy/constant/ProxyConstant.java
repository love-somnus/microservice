package com.somnus.microservice.autoconfigure.proxy.constant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.proxy.constant
 * @title: ProxyConstant
 * @description: TODO
 * @date 2019/6/14 9:55
 */
public class ProxyConstant {
    public static final String CGLIB = "Cglib";

    // JDK Proxy 类型
    public static final String PROXY_TYPE_REFLECTIVE = "Reflective Aop Proxy";

    // CGLIB Proxy 类型
    public static final String PROXY_TYPE_CGLIB = "Cglib Aop Proxy";

    // JDK Proxy 名称关键字
    public static final String JDK_PROXY_NAME_KEY = "com.sun.proxy";

    // CGLIB Proxy 名称关键字
    public static final String CGLIB_PROXY_NAME_KEY = "ByCGLIB";

    public static final String SEPARATOR = ";";
}