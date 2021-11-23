package com.somnus.microservice.limit.starter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.somnus.microservice.limit.starter.aop.LimitImportSelector;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.starter.annotation
 * @title: EnableLimit
 * @description: TODO
 * @date 2019/7/10 17:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LimitImportSelector.class)
public @interface EnableLimit {

}