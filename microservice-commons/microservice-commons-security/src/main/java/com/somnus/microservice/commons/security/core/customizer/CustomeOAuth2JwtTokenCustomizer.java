package com.somnus.microservice.commons.security.core.customizer;

import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.principal.Oauth2User;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p> JWT token 输出增强</p>
 * @author kevin.liu
 * @title: CustomeOAuth2JwtTokenCustomizer
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 15:44
 */
public class CustomeOAuth2JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * Customize the OAuth 2.0 Token attributes.
     * @param context the context containing the OAuth 2.0 Token attributes
     */
    @Override
    public void customize(JwtEncodingContext context) {
        JwtClaimsSet.Builder claims = context.getClaims();

        claims.claim(OAuth2ParameterNames.GRANT_TYPE, context.getAuthorizationGrantType());
        /*claims.claim(OAuth2ParameterNames.CLIENT_ID, context.getAuthorizationGrant().getName());*/

        // 客户端模式不返回具体用户信息
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        Oauth2User oauth2User = (Oauth2User) context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.DETAILS_USER, oauth2User);
    }
}