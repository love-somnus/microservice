package com.somnus.microservice.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

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
     * 单位时间
     * @return int
     */
    int rateInterval();

    /**
     * 单位(默认秒)
     * @return
     */
    TimeUnit rateIntervalUnit() default TimeUnit.SECONDS;

    /**
     * 单位时间产生的令牌个数
     * @return int
     */
    int rate();

    /**
     * 是否限制IP
     * @return boolean
     */
    boolean restrictIp() default false;
}