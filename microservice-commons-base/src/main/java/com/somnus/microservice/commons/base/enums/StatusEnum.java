package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: StatusEnum
 * @description: TODO
 * @date 2019/4/3 9:50
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusEnum {

    VALID(0, "生效"),

    INVALID(1, "失效");

    @Getter
    private Integer status;

    @Getter
    private String desc;

    /**
     * Gets enum.
     *
     * @param status the status
     *
     * @return the enum
     */
    public static StatusEnum statusOf(int status) {
        for (StatusEnum ele : StatusEnum.values()) {
            if (ele.getStatus() == status) {
                return ele;
            }
        }
        return null;
    }

}
