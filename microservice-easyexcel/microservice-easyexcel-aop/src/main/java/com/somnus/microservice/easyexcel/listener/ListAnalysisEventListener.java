package com.somnus.microservice.easyexcel.listener;

import com.alibaba.excel.event.AnalysisEventListener;

import java.util.List;

/**
 * @author kevin.liu
 * @date 2021/12/9 13:11
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

    /**
     * 获取 excel 解析的对象列表
     * @return 集合
     */
    public abstract List<T> getList();

    /**
     * 获取异常校验结果
     * @return 集合
     */
    public abstract List<ErrorMessage> getErrors();

}