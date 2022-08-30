package com.somnus.microservice.xxljob.constants;

import lombok.Getter;

/**
 * @author kevin.liu
 * @title: ExecutorBlockStrategyEnum
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:45
 */
@Getter
public enum ExecutorBlockStrategyEnum {
    SERIAL_EXECUTION("单机串行"),
    DISCARD_LATER("丢弃后续调度"),
    COVER_EARLY("覆盖之前调度")
    ;
    private String remark;

    ExecutorBlockStrategyEnum(String remark) {
        this.remark = remark;
    }
}