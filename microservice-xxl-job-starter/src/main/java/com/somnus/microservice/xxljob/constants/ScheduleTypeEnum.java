package com.somnus.microservice.xxljob.constants;

import lombok.Getter;

/**
 * @author kevin.liu
 * @title: ScheduleTypeEnum
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:47
 */
@Getter
public enum ScheduleTypeEnum {

    NONE("无"),

    CRON("CRON"),

    FIX_RATE("固定速度");

    private String remark;

    ScheduleTypeEnum(String remark) {
        this.remark = remark;
    }
}