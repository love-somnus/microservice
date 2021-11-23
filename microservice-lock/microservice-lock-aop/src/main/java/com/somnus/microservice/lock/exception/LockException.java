package com.somnus.microservice.lock.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.exception
 * @title: LockException
 * @description: TODO
 * @date 2019/6/14 9:55
 */
public class LockException extends RuntimeException {

    private static final long serialVersionUID = -5563106933655728813L;

    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }
}