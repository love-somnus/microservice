package com.somnus.microservice.elasticjob.dynamic.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @author somnus
 * @packageName com.somnus.microservice.elasticjob.dynamic.bean
 * @title: JobProperties
 * @description: TODO
 * @date 2021/10/25 18:05
 */
@Getter
public class JobProperties {

    /**
     * 自定义异常处理类
     * @return
     */
    @JsonProperty("job_exception_handler")
    private final String jobExceptionHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    /**
     * 自定义业务处理线程池
     * @return
     */
    @JsonProperty("executor_service_handler")
    private final String executorServiceHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";

}