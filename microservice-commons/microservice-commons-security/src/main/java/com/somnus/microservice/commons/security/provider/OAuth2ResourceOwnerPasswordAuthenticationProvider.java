package com.somnus.microservice.commons.security.provider;

import com.somnus.microservice.commons.security.token.OAuth2ResourceOwnerPasswordAuthenticationToken;

import java.util.Map;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * <p>处理用户名密码授权</p>
 * @author kevin.liu
 * @title: OAuth2ResourceOwnerPasswordAuthenticationProvider
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 14:52
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerPasswordAuthenticationToken> {

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     * @param authenticationManager
     * @param authorizationService the authorization service
     * @param tokenGenerator the token generator
     * @since 0.2.3
     */
    public OAuth2ResourceOwnerPasswordAuthenticationProvider(AuthenticationManager authenticationManager,
                                                             OAuth2AuthorizationService authorizationService,
                                                             OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
        log.info("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(@NonNull RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        return new UsernamePasswordAuthenticationToken(username, password);
    }

}