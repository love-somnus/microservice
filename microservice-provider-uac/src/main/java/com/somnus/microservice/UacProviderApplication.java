package com.somnus.microservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: UacProviderApplication
 * @description: 用户服务中心启动类
 * @date 2019/3/18 13:43
 */
@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.provider.uac.mapper")
/*@EnableFeignClients(basePackages = "com.somnus.microservice.provider")*/
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.provider")
public class UacProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UacProviderApplication.class, args);
    }

}
