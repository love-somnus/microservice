package com.somnus.microservice.easyexcel.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.support
 * @title: ThreadLocalContext
 * @description: TODO
 * @date 2020/4/22 15:15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalContext {

    private static final ThreadLocal<Map<String, String>> THREAD_CONTEXT = ThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * 取得thread context Map的实例。
     *
     * @return thread context Map的实例
     */
    private static Map<String, String> getContextMap() {
        return THREAD_CONTEXT.get();
    }

    /**
     * 清理线程所有被hold住的对象。以便重用！
     */
    public static void remove() {
        getContextMap().clear();
    }

    /**
     * Remove object.
     *
     * @param key the key
     *
     * @return the object
     */
    public static Object remove(String key) {
        return getContextMap().remove(key);
    }

    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public static void set(String key, String value) {
        getContextMap().put(key, value);
    }

    /**
     * Get object.
     *
     * @param key the key
     *
     * @return the object
     */
    public static String get(String key) {
        return getContextMap().get(key);
    }

}


