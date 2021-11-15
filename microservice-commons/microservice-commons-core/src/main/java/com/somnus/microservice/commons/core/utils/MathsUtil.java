package com.somnus.microservice.commons.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: MathsUtil
 * @description: TODO
 * @date 2019/7/30 15:16
 */
public class MathsUtil {
    private static final char ASTERISK = '*';

    public static Long calculate(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        long result = 1;
        try {
            String[] array = StringUtils.split(value, ASTERISK);
            for (String data : array) {
                result *= Long.parseLong(data.trim());
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return result;
    }
}