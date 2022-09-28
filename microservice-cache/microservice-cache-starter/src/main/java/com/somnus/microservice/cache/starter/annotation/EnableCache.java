package com.somnus.microservice.cache.starter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.somnus.microservice.cache.starter.aop.CacheImportSelector;
import org.springframework.context.annotation.Import;
/**
 * @author Kevin
 * @date 2019/7/5 16:58
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CacheImportSelector.class)
public @interface EnableCache {

}