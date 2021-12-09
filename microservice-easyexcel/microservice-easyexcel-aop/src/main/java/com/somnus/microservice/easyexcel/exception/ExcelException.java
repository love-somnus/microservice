package com.somnus.microservice.easyexcel.exception;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.exception
 * @title: ExcelException
 * @description: TODO
 * @date 2021/6/14 9:55
 */
public class ExcelException extends RuntimeException {

    private static final long serialVersionUID = 2523106933655728813L;

    public ExcelException() {
        super();
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }
}