package com.somnus.microservice.xxljob.constants;

import lombok.Getter;

/**
 * @author kevin.liu
 * @title: MisfireStrategyEnum
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:46
 */
@Getter
public enum MisfireStrategyEnum {
    DO_NOTHING("忽略"),
    FIRE_ONCE_NOW("立即执行一次")
    ;
    private String remark;

    MisfireStrategyEnum(String remark) {
        this.remark = remark;
    }
}