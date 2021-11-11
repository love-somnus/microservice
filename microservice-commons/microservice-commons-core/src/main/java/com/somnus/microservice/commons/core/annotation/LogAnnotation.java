package com.somnus.microservice.commons.core.annotation;

import com.somnus.microservice.commons.core.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.annotation;
 * @title: LogAnnotation
 * @description: 操作日志.
 * @date 2019/3/15 16:23
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    /**
     * 详细描述
     * @return String
     */
    String describe() default "";

    /**
     * 日志类型
     *
     * @return the log type enum
     */
    LogTypeEnum logType() default LogTypeEnum.OPERATION_LOG;

    /**
     * 是否保存请求的参数
     *
     * @return the boolean
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的结果
     *
     * @return the boolean
     */
    boolean isSaveResponseData() default true;
}