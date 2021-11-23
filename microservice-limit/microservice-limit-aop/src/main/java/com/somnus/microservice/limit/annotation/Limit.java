package com.somnus.microservice.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.annotation
 * @title: Limit
 * @description: TODO
 * @date 2019/7/10 16:35
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {
    /**
     * 资源的名字
     * @return String
     */
    String name() default "";

    /**
     * 资源的key
     * @return String
     */
    String key() default "";

    /**
     * 给定的时间段
     * 单位秒
     * @return int
     */
    int limitPeriod();

    /**
     * 最多的访问限制次数
     * @return int
     */
    int limitCount();

    /**
     * 是否限制IP
     * @return boolean
     */
    boolean restrictIp() default true;
}