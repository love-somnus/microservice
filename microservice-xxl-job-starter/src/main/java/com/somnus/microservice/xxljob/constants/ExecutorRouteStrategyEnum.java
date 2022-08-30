package com.somnus.microservice.xxljob.constants;

import lombok.Getter;

/**
 * @author kevin.liu
 * @title: ExecutorRouteStrategyEnum
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:45
 */
@Getter
public enum ExecutorRouteStrategyEnum {
    FIRST("第一个"),
    LAST("最后一个"),
    ROUND("轮询"),
    RANDOM("随机"),
    CONSISTENT_HASH("一致性HASH"),
    LEAST_FREQUENTLY_USED("最不经常使用"),
    LEAST_RECENTLY_USED("最近最久未使用"),
    FAILOVER("故障转移"),
    BUSYOVER("忙碌转移"),
    SHARDING_BROADCAST("分片广播")
    ;
    private String remark;

    ExecutorRouteStrategyEnum(String remark) {
        this.remark = remark;
    }
}