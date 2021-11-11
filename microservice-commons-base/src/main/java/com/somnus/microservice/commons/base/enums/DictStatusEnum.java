package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: DictStatusEnum
 * @description: TODO
 * @date 2019/4/16 17:44
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DictStatusEnum {
    /**
     * 启用
     */
    ENABLE(10, "启用"),
    /**
     * 禁用
     */
    DISABLE(20, "禁用");

    /**
     * The Type.
     */
    @Getter
    int type;
    /**
     * The Name.
     */
    @Getter
    String name;


    /**
     * Gets name.
     *
     * @param type the type
     *
     * @return the name
     */
    public static String getName(int type) {
        for (DictStatusEnum ele : DictStatusEnum.values()) {
            if (type == ele.getType()) {
                return ele.getName();
            }
        }
        return null;
    }

    /**
     * Gets enum.
     *
     * @param type the type
     *
     * @return the enum
     */
    public static DictStatusEnum typeOf(int type) {
        for (DictStatusEnum ele : DictStatusEnum.values()) {
            if (type == ele.getType()) {
                return ele;
            }
        }
        return DictStatusEnum.ENABLE;
    }
}