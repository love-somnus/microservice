package com.somnus.microservice.lock.starter.annotation;

import com.somnus.microservice.lock.starter.aop.LockImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.starter.annotation
 * @title: EnableLock
 * @description: TODO
 * @date 2019/6/14 17:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LockImportSelector.class)
public @interface EnableLock {

}