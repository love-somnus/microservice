package com.somnus.microservice.xxljob.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author kevin.liu
 * @title: Wrapper
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 23:45
 */
public class Wrapper<T> implements Serializable {

    /**
     * 成功码.
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 成功信息.
     */
    public static final String SUCCESS_MESSAGE = "success";

    /**
     * 编号.
     */
    @Getter
    @Builder.Default
    private Integer code = SUCCESS_CODE;

    /**
     * 信息.
     */
    @Getter
    @Builder.Default
    private String msg = SUCCESS_MESSAGE;

    /**
     * 结果数据
     */
    @Getter
    private T content;

    /**
     * Instantiates a new wrapper. default code=200
     */
    Wrapper() {
        this(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     */
    Wrapper(Integer code, String message) {
        this(code, message, null);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     * @param content  the content
     */
    Wrapper(Integer code, String message, T content) {
        super();
        this.code(code).message(message).content(content);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param code the new 编号
     *
     * @return the wrapper
     */
    private Wrapper<T> code(Integer code) {
        this.code = code;
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
     * @param content the new 结果数据
     *
     * @return the wrapper
     */
    public Wrapper<T> content(T content) {
        this.content = content;
        return this;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    @JsonIgnore
    public boolean success() {
        return SUCCESS_CODE == this.code;
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
