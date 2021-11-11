package com.somnus.microservice.commons.redisson.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redisson.exception
 * @title: RedissonException
 * @description: TODO
 * @date 2019/6/14 11:25
 */
public class RedissonException extends RuntimeException {
    private static final long serialVersionUID = 4550515832057492430L;

    public RedissonException() {
        super();
    }

    public RedissonException(String message) {
        super(message);
    }

    public RedissonException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedissonException(Throwable cause) {
        super(cause);
    }
}