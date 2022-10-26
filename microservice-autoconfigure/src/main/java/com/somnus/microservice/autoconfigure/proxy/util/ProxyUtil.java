package com.somnus.microservice.autoconfigure.proxy.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.proxy.util
 * @title: ProxyUtil
 * @description: TODO
 * @date 2019/6/14 9:57
 */
public class ProxyUtil {
    /**
     * 转换Class数组成字符串格式
     * @param parameterTypes
     * @return
     */
    public static String toString(Class<?>[] parameterTypes) {
        if (Objects.isNotEmpty(parameterTypes)) {
            return "";
        }

        return Arrays.stream(parameterTypes)
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(","))
                .trim();
    }

    /**
     * 转换String数组成字符串格式
     * @param values
     * @return
     */
    public static String toString(String[] values) {
        if (Objects.isNotEmpty(values)) {
            return "";
        }

        return String.join(",", values).trim();
    }
}
