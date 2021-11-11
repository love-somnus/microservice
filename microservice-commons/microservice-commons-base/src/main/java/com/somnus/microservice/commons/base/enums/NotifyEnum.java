package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums;
 * @title: NotifyEnum
 * @description: TODO
 * @date 2019/3/22 13:45
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotifyEnum {

    UN_NOTIFY(0, "未通知"),

    NOTIFIED(1, "已通知");

    @Getter
    private Integer code;

    @Getter
    private String message;
}
