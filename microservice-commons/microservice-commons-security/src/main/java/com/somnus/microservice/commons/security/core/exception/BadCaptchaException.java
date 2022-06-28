package com.somnus.microservice.commons.security.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author kevin.liu
 * @title: BadCaptchaException
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/28 16:44
 */
public class BadCaptchaException extends AuthenticationException {

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message.
     * @param msg the detail message
     */
    public BadCaptchaException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message and
     * root cause.
     * @param msg the detail message
     * @param cause root cause
     */
    public BadCaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

}