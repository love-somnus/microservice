package com.somnus.microservice.commons.utils.wrapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.somnus.microservice.commons.utils.PublicUtil;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils.wrapper
 * @title: WrapMapper
 * @description: The class Wrap mapper.
 * @date 2019/3/15 15:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WrapMapper {

    /**
     * Wrap.
     *
     * @param <E>     the element type
     * @param status    the code
     * @param message the message
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> wrap(String status, String message, E result) {
        return new Wrapper<>(status, message, result);
    }

    /**
     * Wrap SUCCESS. code=200
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> ok() {
        return new Wrapper<>();
    }
    /**
     * Ok wrapper.
     *
     * @param <E> the type parameter
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> success(E result) {
        return new Wrapper<>(Wrapper.SUCCESS_STATUS, Wrapper.SUCCESS_MESSAGE, result);
    }

    /**
     * Wrap.
     *
     * @param message the message
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> fail(String message) {
        return wrap(Wrapper.FAIL_STATUS, message, null);
    }

    /**
     * Un wrapper.
     *
     * @param <E>     the element type
     * @param wrapper the wrapper
     *
     * @return the e
     */
    public static <E> E unWrap(Wrapper<E> wrapper) {
        return wrapper.getResult();
    }

    /**
     * Wrap ERROR. code=100
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> illegalArgument(E result) {
        return wrap(Wrapper.ILLEGAL_ARGUMENT_STATUS_, Wrapper.ILLEGAL_ARGUMENT_MESSAGE, result);
    }

    /**
     * Wrap ERROR. code=500
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> error() {
        return wrap(Wrapper.ERROR_STATUS, Wrapper.ERROR_MESSAGE, null);
    }


    /**
     * Wrap EN-ERROR. code=500
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> errorEn() {
        return wrap(Wrapper.ERROR_STATUS, Wrapper.ERROR_MESSAGE_EN, null);
    }

    /**
     * Error wrapper.
     *
     * @param <E>     the type parameter
     * @param message the message
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> error(String message) {
        return wrap(Wrapper.ERROR_STATUS, PublicUtil.isEmpty(message) ? Wrapper.ERROR_MESSAGE : message, null);
    }

    /**
     * Wrap WAIT.
     *
     * @param <E> the element type
     *
     * @return the wrapper
     */
    public static <E> Wrapper<E> waiting() {
        return wrap(Wrapper.WAIT_STATUS, Wrapper.WAIT_MESSAGE, null);
    }

}