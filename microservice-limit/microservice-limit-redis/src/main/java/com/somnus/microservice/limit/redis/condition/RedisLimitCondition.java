package com.somnus.microservice.limit.redis.condition;

import com.somnus.microservice.limit.condition.LimitCondition;
import com.somnus.microservice.limit.constant.LimitConstant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.condition
 * @title: RedisLimitCondition
 * @description: TODO
 * @date 2019/7/10 16:59
 */
public class RedisLimitCondition extends LimitCondition {

    public RedisLimitCondition() {
        super(LimitConstant.LIMIT_TYPE, LimitConstant.LIMIT_TYPE_REDIS);
    }

}