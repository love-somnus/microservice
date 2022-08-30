package com.somnus.microservice.xxljob.request;

import com.somnus.microservice.xxljob.constants.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author kevin.liu
 * @title: DefaultXxlJobAddParam
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:59
 */
@Data
@NoArgsConstructor
public class DefaultXxlJobInfoRequiredParam {

    public DefaultXxlJobInfoRequiredParam(int jobGroup, String jobDesc,  ScheduleTypeEnum scheduleType, String scheduleConf, String executorHandler, String executorParam) {
        this.jobGroup = jobGroup;
        this.jobDesc = jobDesc;
        this.scheduleType = scheduleType;
        this.scheduleConf = scheduleConf;
        this.executorHandler = executorHandler;
        this.executorParam = executorParam;
    }

    public DefaultXxlJobInfoRequiredParam(int jobGroup, String jobDesc, String scheduleConf, String executorHandler, String executorParam) {
        this.jobGroup = jobGroup;
        this.jobDesc = jobDesc;
        this.scheduleConf = scheduleConf;
        this.executorHandler = executorHandler;
        this.executorParam = executorParam;
    }

    /**
     * 执行器主键ID
     */
    @NotNull
    protected int jobGroup;
    /**
     * 任务描述
     */
    @NotEmpty
    protected String jobDesc;
    /**
     * 负责人
     */
    @NotEmpty
    protected String author = "admin";
    /**
     * 报警邮件
     */
    protected String alarmEmail;
    /**
     * 调度类型
     */
    protected ScheduleTypeEnum scheduleType = ScheduleTypeEnum.CRON;
    /**
     * 调度配置，值含义取决于调度类型
     */
    @NotEmpty
    protected String scheduleConf;
    /**
     * 执行器，任务Handler名称
     */
    @NotEmpty
    protected String executorHandler;
    /**
     * 执行器，任务参数
     */
    @NotEmpty
    protected String executorParam;
    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    protected GlueTypeEnum glueType = GlueTypeEnum.BEAN;
    /**
     * 执行器路由策略
     */
    protected ExecutorRouteStrategyEnum executorRouteStrategy = ExecutorRouteStrategyEnum.FAILOVER;
    /**
     * 子任务ID，多个逗号分隔
     */
    protected String childJobId;
    /**
     * 调度过期策略
     */
    protected MisfireStrategyEnum misfireStrategy = MisfireStrategyEnum.DO_NOTHING;
    /**
     * 阻塞处理策略
     */
    protected ExecutorBlockStrategyEnum executorBlockStrategy = ExecutorBlockStrategyEnum.SERIAL_EXECUTION;
    /**
     * 任务执行超时时间，单位秒
     */
    protected int executorTimeout = 0;
    /**
     * 失败重试次数
     */
    protected int executorFailRetryCount = 0;
    /**
     * GLUE备注
     */
    protected String glueRemark = "glueRemark";

}
