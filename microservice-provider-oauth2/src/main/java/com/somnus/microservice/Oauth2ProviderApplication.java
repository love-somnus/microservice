package com.somnus.microservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: Oauth2ProviderApplication
 * @description: 鉴权服务中心启动类
 * @date 2019/3/18 13:43
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.provider.oauth2.mapper")
/*@EnableFeignClients(basePackages = "com.somnus.microservice.provider")*/
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.provider")
public class Oauth2ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ProviderApplication.class, args);
    }

}
