package com.somnus.microservice.commons.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;
/**
 * @author kevin.liu
 * @title: OAuth2SmsAuthenticationToken
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 14:39
 */
public class OAuth2SmsAuthenticationToken extends OAuth2BaseAuthenticationToken {

    public OAuth2SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                        Authentication clientPrincipal,
                                        Set<String> scopes,
                                        Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }

}
