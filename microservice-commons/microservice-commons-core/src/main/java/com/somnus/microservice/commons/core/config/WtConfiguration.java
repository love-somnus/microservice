package com.somnus.microservice.commons.core.config;

import com.somnus.microservice.commons.base.properties.BaiduStatisticsProperties;
import com.somnus.microservice.commons.base.properties.EmailProperties;
import com.somnus.microservice.commons.base.request.BaiduStatisticsHeader;
import com.somnus.microservice.commons.core.generator.IdGenerator;
import com.somnus.microservice.commons.core.generator.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base
 * @title: WtConfiguration
 * @description: TODO
 * @date 2019/4/12 11:01
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
        BaiduStatisticsProperties.class,
        EmailProperties.class
})
public class WtConfiguration {

    private final BaiduStatisticsProperties baidu;

    private final EmailProperties email;

    @Bean
    public IdGenerator idGenerator() {
        return new SnowflakeIdGenerator();
    }

    @Bean
    public BaiduStatisticsHeader baiduStatisticsHeader() {
        return new BaiduStatisticsHeader(baidu);
    }

    @Bean
    public EmailConfig emailConfig() {
        return new EmailConfig(email);
    }
}
