package com.somnus.microservice.commons.base.enums;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @title: HandlerType
 * @description: 处理器.
 * @date 2019/3/15 16:23
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerType {

    String[] values();
}