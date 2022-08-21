package com.somnus.microservice.provider.oauth2.api.exception;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.Mdc.api.exception
 * @title: Oauth2BizException
 * @description: TODO
 * @date 2019/3/28 15:54
 */
@Slf4j
public class Oauth2BizException extends BusinessException {

    private static final long serialVersionUID = 5926842866052951590L;

    /**
     * Instantiates a new Mdc rpc exception.
     */
    public Oauth2BizException() {
    }

    /**
     * Instantiates a new Mdc rpc exception.
     *
     * @param code      the code
     * @param msgFormat the msg format
     * @param args      the args
     */
    public Oauth2BizException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
        log.info("<== MdcRpcException, code:" + this.code + ", message:" + super.getMessage());

    }

    /**
     * Instantiates a new Mdc rpc exception.
     *
     * @param code the code
     * @param msg  the msg
     */
    public Oauth2BizException(int code, String msg) {
        super(code, msg);
        log.info("<== MdcRpcException, code:" + this.code + ", message:" + super.getMessage());
    }

    /**
     * Instantiates a new Mdc rpc exception.
     *
     * @param codeEnum the code enum
     */
    public Oauth2BizException(ErrorCodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.getMsg());
        log.info("<== MdcRpcException, code:" + this.code + ", message:" + super.getMessage());
    }

    /**
     * Instantiates a new Mdc rpc exception.
     *
     * @param codeEnum the code enum
     * @param args     the args
     */
    public Oauth2BizException(ErrorCodeEnum codeEnum, Object... args) {
        super(codeEnum, args);
        log.info("<== MdcRpcException, code:" + this.code + ", message:" + super.getMessage());
    }
}