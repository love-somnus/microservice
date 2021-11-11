package com.somnus.microservice.commons.core.annotation;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.annotation;
 * @title: ValidateAnnotation
 * @description: The interface Validate annotation.
 * @date 2019/3/15 16:25
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateAnnotation {
    /**
     * Is validate boolean.
     *
     * @return the boolean
     */
    boolean isValidate() default true;
}