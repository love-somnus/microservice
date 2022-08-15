package com.somnus.microservice.limit.constant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.constant
 * @title: LimitConstant
 * @description: TODO
 * @date 2019/7/10 16:42
 */
public class LimitConstant {
    public static final String LIMIT_ENABLED = "limit.enabled";

    public static final String LIMIT_TYPE = "limit.type";

    public static final String LIMIT_TYPE_REDIS = "redisLimit";
    public static final String LIMIT_TYPE_REDISSON = "redissonLimit";
    public static final String LIMIT_TYPE_LOCAL = "localLimit";

    public static final String LIMIT_AOP_EXCEPTION_IGNORE = "limit.aop.exception.ignore";

    public static final String LIMIT_SCAN_PACKAGES = "limit.scan.packages";

    public static final String PREFIX = "prefix";

    public static final String FREQUENT_LOG_PRINT = "frequent.log.print";
}
