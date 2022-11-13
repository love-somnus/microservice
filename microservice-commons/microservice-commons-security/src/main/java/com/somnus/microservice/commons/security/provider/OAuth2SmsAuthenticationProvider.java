package com.somnus.microservice.commons.security.provider;

import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.token.OAuth2SmsAuthenticationToken;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * <p>处理短信验证码授权</p>
 * @author kevin.liu
 * @date 2022/6/14 14:37
 */
@Slf4j
public class OAuth2SmsAuthenticationProvider extends OAuth2BaseAuthenticationProvider<OAuth2SmsAuthenticationToken> {

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     * @param authenticationManager the authorization manager
     * @param authorizationService the authorization service
     * @param tokenGenerator the token generator
     * @since 0.2.3
     */
    public OAuth2SmsAuthenticationProvider(AuthenticationManager authenticationManager,
                                           OAuth2AuthorizationService authorizationService,
                                           OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void checkClient(@NonNull RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType(SecurityConstants.SMS))) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error(
                            OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, SecurityConstants.SMS, ERROR_URI
                    )
            );
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken assemble(Map<String, Object> reqParameters) {
        String mobile = (String) reqParameters.get(SecurityConstants.SMS_PARAMETER_NAME);
        String code = (String) reqParameters.get(OAuth2ParameterNames.CODE);
        return new UsernamePasswordAuthenticationToken(mobile, code);
    }

}