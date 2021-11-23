package com.somnus.microservice;

import com.somnus.microservice.limit.starter.annotation.EnableLimit;
import com.somnus.microservice.lock.starter.annotation.EnableLock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: Oauth2ProviderApplication
 * @description: 鉴权服务中心启动类
 * @date 2019/3/18 13:43
 */
@EnableLock
@EnableLimit
@EnableRetry
@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.oauth2.mapper")
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.oauth2.web.rpc")
public class CpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpcProviderApplication.class, args);
    }

}
