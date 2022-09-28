package com.somnus.microservice;

import com.somnus.microservice.cache.starter.annotation.EnableCache;
import com.somnus.microservice.limit.starter.annotation.EnableLimit;
import com.somnus.microservice.lock.starter.annotation.EnableLock;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: Oauth2ProviderApplication
 * @description: 鉴权服务中心启动类
 * @date 2019/3/18 13:43
 */
@EnableLock
@EnableLimit
@EnableCache
@EnableRetry
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.provider.cpc.mapper")
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.provider")
public class CpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpcProviderApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();

        //添加header
        Map<String, SecurityScheme> map = new HashMap<>();
        map.put("Authorization", new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("Authorization"));
        openAPI.components(new Components().securitySchemes(map));
        map.keySet().forEach(key -> openAPI.addSecurityItem(new SecurityRequirement().addList(key)));

        return openAPI
                .servers(Collections.singletonList(new Server().url("http://192.168.97.101:8000/cpc")))
                .info(new Info().title("Spring API")
                        .description("Spring sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}
