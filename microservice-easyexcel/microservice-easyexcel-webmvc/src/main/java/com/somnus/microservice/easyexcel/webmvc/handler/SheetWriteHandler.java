package com.somnus.microservice.easyexcel.webmvc.handler;

import com.somnus.microservice.easyexcel.annotation.ResponseExcel;

import javax.servlet.http.HttpServletResponse;

/**
 * @author kevin.liu
 * @title: SheetWriteHandler
 * @projectName microservice
 * @description: 写出处理器
 * @date 2021/12/9 11:39
 */
public interface SheetWriteHandler {

    /**
     * 是否支持
     * @param obj
     * @return
     */
    boolean support(Object obj);

    /**
     * 校验
     * @param responseExcel 注解
     */
    void check(ResponseExcel responseExcel);

    /**
     * 返回的对象
     * @param o obj
     * @param response 输出对象
     * @param responseExcel 注解
     */
    void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

    /**
     * 写成对象
     * @param o obj
     * @param response 输出对象
     * @param responseExcel 注解
     */
    void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

}
