package com.somnus.microservice.commons.base.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somnus.microservice.commons.base.properties.BaiduStatisticsProperties;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.request
 * @title: BaiduStatisticsHeader
 * @description: TODO
 * @date 2019/12/12 17:43
 */
@Data
public class BaiduStatisticsHeader {

    private String username;

    private String password;

    private String token;

    private String account_type;

    @Transient
    @JsonIgnore
    private BaiduStatisticsProperties properties;

    public BaiduStatisticsHeader(BaiduStatisticsProperties properties){
        this.properties = properties;
        this.username = properties.getUsername();
        this.password = properties.getPassword();
        this.token = properties.getToken();
        this.account_type = properties.getAccount_type();
    }

}
