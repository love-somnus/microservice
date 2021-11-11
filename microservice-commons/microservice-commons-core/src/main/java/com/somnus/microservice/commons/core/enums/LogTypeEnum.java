package com.somnus.microservice.commons.core.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.enums;
 * @title: LogTypeEnum
 * @description: TODO
 * @date 2019/3/15 16:11
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LogTypeEnum {
    /**
     * 操作日志
     */
    OPERATION_LOG("10", "操作日志"),
    /**
     * 登录日志
     */
    LOGIN_LOG("20", "登录日志"),
    /**
     * 异常日志
     */
    EXCEPTION_LOG("30", "异常日志");

    /**
     * The Type.
     */
    @Getter
    String type;
    /**
     * The Name.
     */
    @Getter
    String name;

    /**
     * Gets name.
     * @param type the type
     *
     * @return the name
     */
    public static String getName(String type) {
        for (LogTypeEnum ele : LogTypeEnum.values()) {
            if (type.equals(ele.getType())) {
                return ele.getName();
            }
        }
        return null;
    }

    /**
     * Gets enum.
     * @param type the type
     *
     * @return the enum
     */
    public static LogTypeEnum typeOf(String type) {
        for (LogTypeEnum ele : LogTypeEnum.values()) {
            if (type.equals(ele.getType())) {
                return ele;
            }
        }
        return LogTypeEnum.OPERATION_LOG;
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public static List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = Lists.newArrayList();
        for (LogTypeEnum ele : LogTypeEnum.values()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("key", ele.getType());
            map.put("value", ele.getName());
            list.add(map);
        }
        return list;
    }

}