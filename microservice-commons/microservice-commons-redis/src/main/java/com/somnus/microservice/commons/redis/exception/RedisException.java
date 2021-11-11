package com.somnus.microservice.commons.redis.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redis.exception
 * @title: RedisException
 * @description: TODO
 * @date 2019/7/5 16:43
 */
public class RedisException extends RuntimeException {
    private static final long serialVersionUID = -1137413793056992202L;

    public RedisException() {
        super();
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}