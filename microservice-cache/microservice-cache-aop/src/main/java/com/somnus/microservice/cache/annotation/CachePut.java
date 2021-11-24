package com.somnus.microservice.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache.annotation
 * @title: CachePut
 * @description: TODO
 * @date 2019/7/5 15:33
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CachePut {
    /**
     * 缓存名字
     * @return String
     */
    String name() default "";

    /**
     * 缓存Key
     * @return String
     */
    String key() default "";

    /**
     * 过期时间
     * 单位毫秒，默认60秒
     * @return long
     */
    long expire() default 60000L;
}
