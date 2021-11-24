package com.somnus.microservice.cache;

import org.aopalliance.intercept.MethodInvocation;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache
 * @title: CacheDelegate
 * @description: TODO
 * @date 2019/7/5 15:32
 */
public interface CacheDelegate {
    /**
     * 新增缓存
     * @param invocation
     * @param key
     * @param expire
     * @return
     * @throws Throwable
     */
    Object invokeCacheable(MethodInvocation invocation, String key, long expire) throws Throwable;

    /**
     * 更新缓存
     * @param invocation
     * @param key
     * @param expire
     * @return
     * @throws Throwable
     */
    Object invokeCachePut(MethodInvocation invocation, String key, long expire) throws Throwable;

    /**
     * 清除缓存
     * @param invocation
     * @param key
     * @param name
     * @param allEntries
     * @param beforeInvocation
     * @return
     * @throws Throwable
     */
    Object invokeCacheEvict(MethodInvocation invocation, String key, String name, boolean allEntries, boolean beforeInvocation) throws Throwable;
}
