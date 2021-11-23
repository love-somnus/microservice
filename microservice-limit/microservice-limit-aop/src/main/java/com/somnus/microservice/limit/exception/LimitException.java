package com.somnus.microservice.limit.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.exception
 * @title: LockException
 * @description: TODO
 * @date 2019/6/14 9:55
 */
public class LimitException extends RuntimeException {

    private static final long serialVersionUID = -5523106933655728813L;

    public LimitException() {
        super();
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }
}