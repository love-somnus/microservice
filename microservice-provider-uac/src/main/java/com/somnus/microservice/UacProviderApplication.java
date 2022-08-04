package com.somnus.microservice;

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
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.util.Collections;
/*import springfox.documentation.oas.annotations.EnableOpenApi;*/

/**
 * @author Kevin
 * @packageName com.somnus.microservice
 * @title: UacProviderApplication
 * @description: 用户服务中心启动类
 * @date 2019/3/18 13:43
 */
/*@EnableOpenApi*/
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "com.somnus.microservice.provider.uac.mapper")
/*@EnableFeignClients(basePackages = "com.somnus.microservice.provider")*/
@EnableReactiveFeignClients(basePackages = "com.somnus.microservice.provider")
public class UacProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UacProviderApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        /*OpenAPI openAPI = new OpenAPI();

        Map<String, SecurityScheme> map = new HashMap<>();
        map.put("Authorization", new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("Authorization"));

        openAPI.components(new Components().securitySchemes(map));
        map.keySet().forEach(key -> openAPI.addSecurityItem(new SecurityRequirement().addList(key)));*/

        return new OpenAPI()
                .servers(Collections.singletonList(new Server().url("http://192.168.97.101:8000/uac")))
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("Authorization")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .info(new Info().title("Spring API")
                        .description("Spring sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}
