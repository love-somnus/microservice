package com.somnus.microservice.commons.core.annotation;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.annotation;
 * @title: NotDisplaySql
 * @description: 配合 SqlLogInterceptor 对指定方法 禁止打印SQL到控制台
 * @date 2019/3/15 16:24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotDisplaySql {
}
