package com.somnus.microservice.easyexcel.annotation;

import com.somnus.microservice.easyexcel.listener.DefaultAnalysisEventListener;
import com.somnus.microservice.easyexcel.listener.ListAnalysisEventListener;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

    /**
     * 前端上传字段名称 file
     */
    String fileName() default "file";

    /**
     * 读取的监听器类
     * @return readListener
     */
    Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

    /**
     * 是否跳过空行
     * @return 默认跳过
     */
    boolean ignoreEmptyRow() default false;
}
