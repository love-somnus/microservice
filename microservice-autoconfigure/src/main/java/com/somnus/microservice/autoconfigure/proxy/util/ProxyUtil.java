package com.somnus.microservice.autoconfigure.proxy.util;

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

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append("," + clazz.getCanonicalName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
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

        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append("," + value);
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }
}
