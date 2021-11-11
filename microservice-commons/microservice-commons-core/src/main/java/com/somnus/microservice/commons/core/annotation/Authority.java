package com.somnus.microservice.commons.core.annotation;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.annotation;
 * @title: NotDisplaySql
 * @description:
 * @date 2019/3/15 16:24
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Authority {

}
