package com.somnus.microservice.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevin.liu
 * @title: IngoreUrlsConfig
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/23 20:50
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "secure.ignore")
@EnableConfigurationProperties({
        IngoreUrlsConfig.class
})
public class IngoreUrlsConfig {

    private String[] urls;
}
