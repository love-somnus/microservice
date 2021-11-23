package com.somnus.microservice.lock.local.condition;

import com.somnus.microservice.lock.condition.LockCondition;
import com.somnus.microservice.lock.constant.LockConstant;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.local.condition
 * @title: LocalLockCondition
 * @description: TODO
 * @date 2019/6/14 18:00
 */
public class LocalLockCondition extends LockCondition {

    public LocalLockCondition() {
        super(LockConstant.LOCK_TYPE, LockConstant.LOCK_TYPE_LOCAL);
    }

}