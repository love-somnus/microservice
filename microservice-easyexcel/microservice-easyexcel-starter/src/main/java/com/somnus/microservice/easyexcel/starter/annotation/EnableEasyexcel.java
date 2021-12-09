package com.somnus.microservice.easyexcel.starter.annotation;

import com.somnus.microservice.easyexcel.starter.aop.EasyexcelImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.starter.annotation
 * @title: EnableEasyexcel
 * @description: TODO
 * @date 2021/7/10 17:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasyexcelImportSelector.class)
public @interface EnableEasyexcel {

}