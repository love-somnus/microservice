package com.somnus.microservice.lock.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.entity
 * @title: LockType
 * @description: TODO
 * @date 2019/6/14 14:52
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LockType {
    /**
     * 普通锁
     */
    LOCK("Lock"),

    /**
     * 读锁
     */
    READ_LOCK("ReadLock"),

    /**
     * 写锁
     */
    WRITE_LOCK("WriteLock");

    private String value;

    public static LockType fromString(String value) {
        for (LockType type : LockType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
