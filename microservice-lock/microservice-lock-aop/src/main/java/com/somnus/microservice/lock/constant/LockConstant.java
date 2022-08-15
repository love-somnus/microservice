package com.somnus.microservice.lock.constant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.constant
 * @title: LockConstant
 * @description: TODO
 * @date 2019/6/14 14:56
 */
public class LockConstant {
    public static final String LOCK_ENABLED = "lock.enabled";

    public static final String LOCK_TYPE = "lock.type";

    public static final String LOCK_TYPE_REDIS = "redisLock";
    public static final String LOCK_TYPE_ZOOKEEPER = "zookeeperLock";
    public static final String LOCK_TYPE_LOCAL = "localLock";

    public static final String LOCK_AOP_EXCEPTION_IGNORE = "lock.aop.exception.ignore";

    public static final String LOCK_SCAN_PACKAGES = "lock.scan.packages";

    public static final String PREFIX = "prefix";

    public static final String FREQUENT_LOG_PRINT = "frequent.log.print";
}