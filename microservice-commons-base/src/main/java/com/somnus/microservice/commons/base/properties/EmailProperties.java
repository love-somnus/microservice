package com.somnus.microservice.commons.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @title: EmailProperties
 * @projectName webteam
 * @description: TODO
 * @author kevin.liu
 * @date 2021/5/12 19:13
 */
@Data
@ConfigurationProperties(prefix = "wt.email")
public class EmailProperties {

    private List<Properties> configs;

    @Data
    public static class Properties {
        /**
         * 设置Email的组织
         */
        private String org;

        /**
         * 设置Email的host
         */
        private String host;

        /**
         * 设置Email的端口号
         */
        private Integer port;

        /**
         * 设置Email的协议
         */
        private String protocol;

        /**
         * 设置Email的用户名
         */
        private String username;

        /**
         * 设置Email的密码
         */
        private String password;

        private String defaultEncoding;
    }

}
