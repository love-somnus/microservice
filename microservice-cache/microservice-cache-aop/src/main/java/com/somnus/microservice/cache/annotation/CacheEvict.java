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
 * @title: CacheEvict
 * @description: TODO
 * @date 2019/7/5 15:33
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
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
     * 是否全部清除缓存内容
     * @return boolean
     */
    boolean allEntries() default false;

    /**
     * 缓存清理是在方法调用前还是调用后
     * @return boolean
     */
    boolean beforeInvocation() default false;
}