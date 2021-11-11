package com.somnus.microservice.commons.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.properties
 * @title: BaiduStatisticsProperties
 * @description: TODO
 * @date 2019/12/12 17:32
 */
@Data
@ConfigurationProperties(prefix = "somnus.baidu")
public class BaiduStatisticsProperties {

    private String username;

    private String password;

    private String token;

    private String account_type;
}
