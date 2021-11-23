package com.somnus.microservice.lock.redis.condition;

import com.somnus.microservice.lock.condition.LockCondition;
import com.somnus.microservice.lock.constant.LockConstant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.redis.condition
 * @title: RedisLockCondition
 * @description: TODO
 * @date 2019/6/14 15:39
 */
public class RedisLockCondition extends LockCondition {

    public RedisLockCondition() {
        super(LockConstant.LOCK_TYPE, LockConstant.LOCK_TYPE_REDIS);
    }

}