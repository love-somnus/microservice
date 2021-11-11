package com.somnus.microservice.commons.base.request;

import com.somnus.microservice.commons.utils.JacksonUtil;
import lombok.Data;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.request
 * @title: BaiduStatisticsMessage
 * @description: TODO
 * @date 2019/12/13 15:45
 */
@Data
public class BaiduStatisticsMessage {

    private BaiduStatisticsHeader header;

    private BaiduStatisticsBody body;

    public BaiduStatisticsMessage(BaiduStatisticsHeader header){
        this.header = header;
    }

    public BaiduStatisticsMessage(BaiduStatisticsHeader header, BaiduStatisticsBody body){
        this.header = header;
        this.body = body;
    }

    @Override
    public String toString(){
        return JacksonUtil.toJson(this);
    }

}
