package com.somnus.microservice.oauth2.config.repository;

import cn.hutool.core.util.BooleanUtil;
import com.somnus.microservice.oauth2.model.domain.Oauth2ClientDetails;
import com.somnus.microservice.oauth2.service.Oauth2ClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
/**
 * @author kevin.liu
 * @title: RedisRegisteredClientRepository
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 20:16
 */
@RequiredArgsConstructor
public class RedisRegisteredClientRepository implements RegisteredClientRepository {

    private final Oauth2ClientDetailsService service;

    /**
     * 刷新令牌有效期默认 30 填
     */
    private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    /**
     * 请求令牌有效期默认 12 小时
     */
    private final static int accessTokenValiditySeconds = 60 * 60 * 12;

    /**
     * Saves the registered client.
     *
     * <p>
     * IMPORTANT: Sensitive information should be encoded externally from the
     * implementation, e.g. {@link RegisteredClient#getClientSecret()}
     * @param registeredClient the {@link RegisteredClient}
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the registered client identified by the provided {@code id}, or
     * {@code null} if not found.
     * @param id the registration identifier
     * @return the {@link RegisteredClient} if found, otherwise {@code null}
     */
    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {

        Oauth2ClientDetails clientDetails = service.findByClientId(clientId).orElseThrow(() -> new UsernameNotFoundException("clientId: [" + clientId + "] do not exist!"));;

        RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
                .clientId(clientDetails.getClientSecret())
                .clientSecret(clientDetails.getClientSecret());

        // 授权模式
        Optional.ofNullable(clientDetails.getAuthorizationGrantTypes())
                .ifPresent(grants -> StringUtils.commaDelimitedListToSet(grants)
                        .forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));

        // 授权方法
        Optional.ofNullable(clientDetails.getClientAuthenticationMethods())
                .ifPresent(methods -> StringUtils.commaDelimitedListToSet(methods)
                        .forEach(s -> builder.clientAuthenticationMethod(new ClientAuthenticationMethod(s))));

        // scope 客户端申请的作用域，也可以理解这个客户端申请访问用户的哪些信息，比如：获取用户信息，获取用户照片等
        Optional.ofNullable(clientDetails.getScopes())
                .ifPresent(scope -> builder.scopes(scopes -> scopes.addAll(StringUtils.commaDelimitedListToSet(scope))));

        // 回调地址
        Optional.ofNullable(clientDetails.getRedirectUris()).ifPresent(redirectUri -> builder.redirectUris(redirectUris -> redirectUris.addAll(Collections.singletonList(redirectUri))));

        return builder
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)//REFERENCE
                        // accessToken 的有效期
                        .accessTokenTimeToLive(Duration.ofHours(Optional.ofNullable(clientDetails.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
                        // refreshToken 的有效期
                        .refreshTokenTimeToLive(Duration.ofHours(Optional.ofNullable(clientDetails.getRefreshTokenValidity()).orElse(refreshTokenValiditySeconds)))
                        // 是否可重用刷新令牌
                        .reuseRefreshTokens(true)
                        .build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoApprove())).build())
                .build();

    }

}
