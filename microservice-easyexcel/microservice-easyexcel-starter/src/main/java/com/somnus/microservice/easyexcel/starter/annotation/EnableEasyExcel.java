package com.somnus.microservice.easyexcel.starter.annotation;

import com.somnus.microservice.easyexcel.starter.aop.EasyExcelImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Kevin
 * @date 2021/7/10 17:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasyExcelImportSelector.class)
public @interface EnableEasyExcel {

}