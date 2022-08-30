package com.somnus.microservice.xxljob.service;

import com.somnus.microservice.xxljob.response.JobInfoPageResult;
import com.somnus.microservice.xxljob.request.DefaultXxlJobInfoRequiredParam;
import com.somnus.microservice.xxljob.request.XxlJobInfo;
import com.somnus.microservice.xxljob.request.XxlJobInfoRequiredParam;

import java.util.List;

/**
 * @author kevin.liu
 * @title: XxlJobService
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:24
 */
public interface XxlJobService {

    String jobAddPath = "/jobinfo/add";
    String jobRemovePath = "/jobinfo/remove";
    String jobUpdatePath = "/jobinfo/update";
    String jobStartPath = "/jobinfo/start";
    String jobStopPath = "/jobinfo/stop";
    String jobTriggerPath = "/jobinfo/trigger";
    String jobPageListPath = "/jobinfo/pageList";
    String jobNextTriggerTimePath = "/jobinfo/nextTriggerTime";

    JobInfoPageResult pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 包含添加job的所有参数
     *
     * @param jobInfo 任务信息
     * @return 任务id
     */
    Integer add(XxlJobInfo jobInfo);

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样
     * @param addParam
     * @return
     */
    Integer add(XxlJobInfoRequiredParam addParam);

    /**
     * 通过必要参数添加job，其它参数和通过网页添加job的默认参数一样。但是可以修改默认参数
     * @param defaultXxlJobAddParam
     * @return
     */
    Integer add(DefaultXxlJobInfoRequiredParam defaultXxlJobAddParam);

    /**
     * update job
     *
     * @param jobInfo 任务信息
     */
    void update(XxlJobInfo jobInfo);

    /**
     * start job
     *
     * @param id 任务id
     */
    Integer start(int id);

    /**
     * stop job
     *
     * @param id 任务id
     */
    void stop(int id);

    /**
     * remove job
     * 	 *
     * @param id 任务id
     */
    void remove(int id);

    /**
     * 取消任务(先暂停，后删除)
     * @param id
     */
    void cancle(int id);

    /**
     * 注册任务(先添加，后启动)
     * @param defaultXxlJobAddParam
     */
    Integer register(DefaultXxlJobInfoRequiredParam defaultXxlJobAddParam);

    void triggerJob(int id, String executorParam, String addressList);

    List<String> nextTriggerTime(String scheduleType, String scheduleConf);
}
