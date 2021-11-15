package com.somnus.microservice.commons.base.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.wrapper
 * @title: Wrapper
 * @description: The class Wrap.
 * @date 2019/3/15 15:01
 */
@Data
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Wrapper<T> implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 4893280118017319089L;

    /**
     * 成功码.
     */
    /*public static final int SUCCESS_CODE = 200;*/
    public static final String SUCCESS_STATUS = "ok";

    /**
     * 成功信息.
     */
    public static final String SUCCESS_MESSAGE = "success";

    /**
     * 错误码.
     */
    public static final String FAIL_STATUS = "fail";

    /**
     * 错误信息.
     */
    public static final String FAIL_MESSAGE = "业务异常";

    /**
     * 错误码.
     */
    /*public static final int ERROR_CODE = 500;*/
    public static final String ERROR_STATUS = "error";

    /**
     * 错误信息.
     */
    public static final String ERROR_MESSAGE = "系统异常";

    /**
     * 错误信息-英文.
     */
    public static final String ERROR_MESSAGE_EN = "systemError";

    /**
     * 错误码：参数非法
     */
    /*public static final int ILLEGAL_ARGUMENT_CODE_ = 100;*/
    public static final String ILLEGAL_ARGUMENT_STATUS_ = "illegal";

    /**
     * 错误信息：参数非法
     */
    public static final String ILLEGAL_ARGUMENT_MESSAGE = "参数非法";

    /**
     * 错误码.
     */
    public static final String WAIT_STATUS = "wait";

    /**
     * 错误信息.
     */
    public static final String WAIT_MESSAGE = "网络故障";

    /**
     * 编号.
     */
    @Builder.Default
    private String status = Wrapper.SUCCESS_STATUS;

    /**
     * 信息.
     */
    @Builder.Default
    private String msg = Wrapper.SUCCESS_MESSAGE;

    /**
     * 结果数据
     */
    private T result;

    /**
     * Instantiates a new wrapper. default code=200
     */
    Wrapper() {
        this(SUCCESS_STATUS, SUCCESS_MESSAGE);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param status    the code
     * @param message the message
     */
    Wrapper(String status, String message) {
        this(status, message, null);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param status    the status
     * @param message the message
     * @param result  the result
     */
    Wrapper(String status, String message, T result) {
        super();
        this.code(status).message(message).result(result);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param status the new 编号
     *
     * @return the wrapper
     */
    private Wrapper<T> code(String status) {
        this.status = status;
        return this;
    }

    /**
     * Sets the 信息 , 返回自身的引用.
     *
     * @param msg the new 信息
     *
     * @return the wrapper
     */
    private Wrapper<T> message(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * Sets the 结果数据 , 返回自身的引用.
     *
     * @param result the new 结果数据
     *
     * @return the wrapper
     */
    public Wrapper<T> result(T result) {
        this.result = result;
        return this;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    @JsonIgnore
    public boolean success() {
        return Wrapper.SUCCESS_STATUS == this.status;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE != this.code
     *
     * @return code !=200,true;否则 false.
     */
    @JsonIgnore
    public boolean error() {
        return !success();
    }

}