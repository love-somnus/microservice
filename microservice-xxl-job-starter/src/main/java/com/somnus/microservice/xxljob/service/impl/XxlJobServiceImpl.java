package com.somnus.microservice.xxljob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.common.collect.ImmutableMap;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.somnus.microservice.commons.base.utils.Objects;
import com.somnus.microservice.xxljob.autoconfigure.XxlJobProperties;
import com.somnus.microservice.xxljob.request.XxlJobInfoRequiredParam;
import com.somnus.microservice.xxljob.response.JobInfoPageResult;
import com.somnus.microservice.xxljob.response.HttpHeader;
import com.somnus.microservice.xxljob.constants.*;
import com.somnus.microservice.xxljob.request.DefaultXxlJobInfoRequiredParam;
import com.somnus.microservice.xxljob.request.XxlJobInfo;
import com.somnus.microservice.xxljob.service.XxlJobService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author kevin.liu
 * @title: XxlJobServiceImpl
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:43
 */
@Slf4j
public class XxlJobServiceImpl implements XxlJobService {

    private HttpHeader loginHeader;

    private XxlJobProperties properties;

    private int timeout;

    public XxlJobServiceImpl(HttpHeader loginHeader, XxlJobProperties properties) {
        this.loginHeader = loginHeader;
        this.properties = properties;
        this.timeout = this.properties.getAdmin().getConnectionTimeOut();
    }

    @Override
    public JobInfoPageResult pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

        String url = properties.getAdmin().getAddresses() + jobPageListPath;

        HttpRequest httpRequest = HttpRequest.get(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start",start);
        paramMap.put("length",length);
        paramMap.put("jobGroup",jobGroup);
        paramMap.put("triggerStatus",triggerStatus);
        paramMap.put("jobDesc",jobDesc);
        paramMap.put("executorHandler",executorHandler);
        paramMap.put("author",author);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();
        int status = response.getStatus();
        String body = response.body();
        Assert.isTrue(status == 200);

        return JacksonUtil.parseJson(body, new TypeReference<JobInfoPageResult>(){});
    }

    @Override
    public Integer add(XxlJobInfo jobInfo) {

        String url = properties.getAdmin().getAddresses() + jobAddPath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(), loginHeader.getHeaderValue());

        Map<String, Object> paramMap = BeanUtil.beanToMap(jobInfo);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        Assert.isTrue(status == 200, wrapper.getMsg());

        log.info("xxl-job动态添加任务响应报文:{}", JacksonUtil.toJson(wrapper));

        return Integer.parseInt(wrapper.getContent());
    }

    @Override
    public Integer add(XxlJobInfoRequiredParam param) {

        DefaultXxlJobInfoRequiredParam defaultXxlJobAddParam = Objects.convert(param, DefaultXxlJobInfoRequiredParam.class);

        return this.add(defaultXxlJobAddParam);
    }

    @Override
    public Integer add(DefaultXxlJobInfoRequiredParam param) {

        XxlJobInfo jobInfo = Objects.convert(param, XxlJobInfo.class);

        Optional.ofNullable(param.getScheduleType()).ifPresent(value -> jobInfo.setScheduleType(value.name()));

        Optional.ofNullable(param.getMisfireStrategy()).ifPresent(value -> jobInfo.setMisfireStrategy(value.name()));

        Optional.ofNullable(param.getExecutorRouteStrategy()).ifPresent(value -> jobInfo.setExecutorRouteStrategy(value.name()));

        Optional.ofNullable(param.getExecutorBlockStrategy()).ifPresent(value -> jobInfo.setExecutorBlockStrategy(value.name()));

        Optional.ofNullable(param.getGlueType()).ifPresent(value -> jobInfo.setGlueType(value.name()));

        return this.add(jobInfo);
    }

    @Override
    public void update(XxlJobInfo jobInfo) {

        String url = properties.getAdmin().getAddresses() + jobUpdatePath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String, Object> paramMap = BeanUtil.beanToMap(jobInfo);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        Assert.isTrue(status == 200, wrapper.getMsg());
    }

    @Override
    public Integer start(int id) {

        String url = properties.getAdmin().getAddresses() + jobStartPath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String,Object> paramMap = Collections.singletonMap("id", id);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        Assert.isTrue(status == 200, wrapper.getMsg());

        log.info("xxl-job动态启动任务响应报文:{}", JacksonUtil.toJson(wrapper));

        return id;
    }

    @Override
    public void stop(int id) {

        String url = properties.getAdmin().getAddresses() + jobStopPath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String,Object> paramMap = Collections.singletonMap("id", id);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        log.info("xxl-job动态停止任务响应报文:{}", JacksonUtil.toJson(wrapper));

        Assert.isTrue(status == 200, wrapper.getMsg());
    }

    @Override
    public void remove(int id) {

        String url = properties.getAdmin().getAddresses() + jobRemovePath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String,Object> paramMap = Collections.singletonMap("id", id);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        log.info("xxl-job动态移除任务响应报文:{}", JacksonUtil.toJson(wrapper));

        Assert.isTrue(status == 200, wrapper.getMsg());
    }

    @Override
    public void cancle(int id) {
        this.stop(id);
        this.remove(id);
    }

    @Override
    public Integer register(DefaultXxlJobInfoRequiredParam defaultXxlJobAddParam) {
        Integer taskId = this.add(defaultXxlJobAddParam);

        return this.start(taskId);
    }

    @Override
    public void triggerJob(int id, String executorParam, String addressList) {

        String url = properties.getAdmin().getAddresses() + jobTriggerPath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String,Object> paramMap = ImmutableMap.of("id", id, "executorParam", executorParam, "addressList",addressList);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        Assert.isTrue(status == 200, wrapper.getMsg());
    }

    @Override
    public List<String> nextTriggerTime(String scheduleType, String scheduleConf) {

        String url = properties.getAdmin().getAddresses() + jobNextTriggerTimePath;

        HttpRequest httpRequest = HttpRequest.post(url);

        httpRequest.header(loginHeader.getHeaderName(),loginHeader.getHeaderValue());

        Map<String,Object> paramMap = ImmutableMap.of("scheduleType", scheduleType, "scheduleConf",scheduleConf);

        HttpResponse response = httpRequest.form(paramMap).timeout(timeout).execute();

        int status = response.getStatus();

        String body = response.body();

        Wrapper<List<String>> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<List<String>>>(){});

        Assert.isTrue(status == 200, wrapper.getMsg());

        return wrapper.getContent();
    }

}