package com.somnus.microservice.commons.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;
/**
 * <p>密码授权token信息</p>
 * @author kevin.liu
 * @title: OAuth2ResourceOwnerPasswordAuthenticationToken
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 14:53
 */
public class OAuth2ResourceOwnerPasswordAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

    public OAuth2ResourceOwnerPasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }

}