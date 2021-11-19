package com.somnus.microservice.gateway.config;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.gateway.config
 * @title: SwaggerProvider
 * @description: Swagger3聚合配置
 * @date 2019/3/29 10:33
 */
@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";

    private final RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;

    public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream()
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition ->
                        routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            String genkey = predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0");
                            SwaggerResource swaggerResource = new SwaggerResource();
                            swaggerResource.setName(routeDefinition.getId());
                            swaggerResource.setLocation(genkey.replace("/**", API_URI));
                            swaggerResource.setSwaggerVersion("2.0");
                            resources.add(swaggerResource);
                        })
                );
        return resources;
    }

}