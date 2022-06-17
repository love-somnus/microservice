package com.somnus.microservice.commons.security.core.customizer;

import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.principal.Oauth2User;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>token 输出增强</p>
 * @author kevin.liu
 * @title: CustomeOAuth2TokenCustomizer
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 15:44
 */
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    /**
     * Customize the OAuth 2.0 Token attributes.
     * @param context the context containing the OAuth 2.0 Token attributes
     */
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        claims.claim(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);

        String clientId = context.getAuthorizationGrant().getName();
        claims.claim(SecurityConstants.CLIENT_ID, clientId);

        // 客户端模式不返回具体用户信息
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        Oauth2User oauth2User = (Oauth2User) context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.DETAILS_USER, oauth2User);
    }

}