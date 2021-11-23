package com.somnus.microservice.autoconfigure.proxy.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.autoconfigure.proxy.exception
 * @title: ProxyException
 * @description: TODO
 * @date 2019/6/14 9:55
 */
public class ProxyException extends RuntimeException {
    private static final long serialVersionUID = -5563106933655728813L;

    public ProxyException() {
        super();
    }

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyException(Throwable cause) {
        super(cause);
    }
}