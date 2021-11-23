package com.somnus.microservice.autoconfigure.selector;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.selector
 * @title: KeyUtil
 * @description: TODO
 * @date 2019/6/14 15:09
 */
public class KeyUtil {

    public static String getCompositeKey(String prefix, String name, String key) {
        return prefix + ":" + name + ":" + key;
    }

    public static String getCompositeWildcardKey(String prefix, String name) {
        return prefix + ":" + name + "*";
    }

    public static String getCompositeWildcardKey(String key) {
        return key + "*";
    }
}