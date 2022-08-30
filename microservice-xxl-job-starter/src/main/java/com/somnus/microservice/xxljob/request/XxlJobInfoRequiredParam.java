package com.somnus.microservice.xxljob.request;

import com.somnus.microservice.xxljob.constants.ScheduleTypeEnum;
import lombok.Data;

/**
 * @author kevin.liu
 * @title: XxlJobInfoAddParam
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 17:08
 */
@Data
public class XxlJobInfoRequiredParam {

    /**
     * 执行器主键ID
     */
    private int jobGroup;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 负责人
     */
    private String author;
    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 调度类型
     */
    protected ScheduleTypeEnum scheduleType = ScheduleTypeEnum.CRON;
    /**
     * 调度配置，值含义取决于调度类型
     */
    protected String scheduleConf;

    /**
     * 执行器，任务Handler名称
     */
    protected String executorHandler;
    /**
     * 执行器，任务参数
     */
    protected String executorParam;
}
