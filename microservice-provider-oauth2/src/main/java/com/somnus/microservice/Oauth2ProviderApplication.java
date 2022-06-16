package com.somnus.microservice;

import com.somnus.microservice.cache.starter.annotation.EnableCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: Oauth2ProviderApplication
 * @description: 鉴权服务中心启动类
 * @date 2022/3/18 13:43
 */
@EnableCache
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.oauth2.mapper")
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.oauth2.web.rpc")
public class Oauth2ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ProviderApplication.class, args);
    }

}
