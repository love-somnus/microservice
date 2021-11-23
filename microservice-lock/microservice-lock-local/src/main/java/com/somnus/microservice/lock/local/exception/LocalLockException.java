package com.somnus.microservice.lock.local.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.local.exception
 * @title: ProxyException
 * @description: TODO
 * @date 2019/6/14 9:55
 */
public class LocalLockException extends RuntimeException {
    private static final long serialVersionUID = -5563106933655728813L;

    public LocalLockException() {
        super();
    }

    public LocalLockException(String message) {
        super(message);
    }

    public LocalLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalLockException(Throwable cause) {
        super(cause);
    }
}