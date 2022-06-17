package com.somnus.microservice.commons.security.core.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
/**
 * @author kevin.liu
 * @title: ResourceServerAutoConfiguration
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/17 17:21
 */
@RequiredArgsConstructor
public class ResourceServerAutoConfiguration {

    /**
     * 资源服务器toke内省处理器
     * @param authorizationService token 存储实现
     * @return TokenIntrospector
     */
    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new CustomOpaqueTokenIntrospector(authorizationService);
    }

}
