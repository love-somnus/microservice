package com.somnus.microservice.commons.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils.annotation
 * @title: NoNeedAccessAuthentication
 * @description: TODO
 * @date 2019/4/16 18:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoNeedAccessAuthentication {

}
