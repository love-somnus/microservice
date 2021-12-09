package com.somnus.microservice.easyexcel.annotation;

import com.somnus.microservice.easyexcel.head.HeadGenerator;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {

    int sheetNo() default -1;

    /**
     * sheet name
     */
    String sheetName();

    /**
     * 包含字段
     */
    String[] includes() default {};

    /**
     * 排除字段
     */
    String[] excludes() default {};

    /**
     * 头生成器
     */
    Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
