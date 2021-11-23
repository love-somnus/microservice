package com.somnus.microservice.limit.local.condition;

import com.somnus.microservice.limit.condition.LimitCondition;
import com.somnus.microservice.limit.constant.LimitConstant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.local.condition
 * @title: LocalLimitCondition
 * @description: TODO
 * @date 2019/7/10 16:48
 */
public class LocalLimitCondition extends LimitCondition {

    public LocalLimitCondition() {
        super(LimitConstant.LIMIT_TYPE, LimitConstant.LIMIT_TYPE_LOCAL);
    }
}