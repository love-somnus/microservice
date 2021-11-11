package com.somnus.microservice.commons.base.exception;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.MessageFormat;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.exception;
 * @title: BusinessException
 * @description: 业务异常
 * @date 2019/3/15 15:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    /** 异常码 */
    protected int code;

    private static final long serialVersionUID = 3160241586346324994L;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
    }

    public BusinessException(ErrorCodeEnum codeEnum, Object... args) {
        super(MessageFormat.format(codeEnum.getMsg(), args));
        this.code = codeEnum.getCode();
    }
}