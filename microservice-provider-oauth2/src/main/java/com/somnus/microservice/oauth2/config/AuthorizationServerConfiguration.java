package com.somnus.microservice.oauth2.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.somnus.microservice.commons.security.core.customizer.CustomeOAuth2JwtTokenCustomizer;
import lombok.RequiredArgsConstructor;
import com.somnus.microservice.commons.base.utils.JwksUtil;
import com.somnus.microservice.commons.security.converter.OAuth2PasswordAuthenticationConverter;
import com.somnus.microservice.commons.security.converter.OAuth2SmsAuthenticationConverter;
import com.somnus.microservice.commons.security.core.FormIdentityLoginConfigurer;
import com.somnus.microservice.commons.security.core.UserDetailsAuthenticationProvider;
import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.handler.AuthenticationFailureEventHandler;
import com.somnus.microservice.commons.security.handler.AuthenticationSuccessEventHandler;
import com.somnus.microservice.commons.security.provider.OAuth2PasswordAuthenticationProvider;
import com.somnus.microservice.commons.security.provider.OAuth2SmsAuthenticationProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * <p>Description: 认证服务器配置 </p>
 * @author kevin.liu
 * @title: AuthorizationServerConfiguration
 * @projectName oauth2
 * @date 2021/11/16 13:55
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        OAuth2AuthorizationServerConfigurer<HttpSecurity> configurer = authorizationServerConfigurer.tokenEndpoint(
            // 个性化认证授权端点
            (tokenEndpoint) -> {
                tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
            }
        )
         // 授权码端点个性化confirm页面
        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI));

        http.apply(configurer);

        // TODO 你可以根据需求对authorizationServerConfigurer进行一些个性化配置
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        // @formatter:off 拦截 授权服务器相关的请求端点
        DefaultSecurityFilterChain securityFilterChain = http.requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                /* redis存储token的实现 */
                .apply(configurer.authorizationService(authorizationService))
                /* 授权码登录的登录页个性化 */
                .and().apply(new FormIdentityLoginConfigurer()).and().build();
        // @formatter:on

        /* 注入自定义授权模式实现  */
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /*@Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                *//* 客户端ID和密码 *//*
                .clientId("client")
                .clientSecret("secret")
                *//* 授权方法 *//*
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                *//* 授权类型 *//*
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // 重定向url
                .redirectUris(redirectUris -> redirectUris.addAll(Collections.singletonList("https://www.baidu.com")))
                // 客户端申请的作用域，也可以理解这个客户端申请访问用户的哪些信息，比如：获取用户信息，获取用户照片等
                .scopes(scopes -> scopes.addAll(Arrays.asList(OidcScopes.OPENID, "message.read", "message.write")))
                .tokenSettings(TokenSettings.builder()
                        // accessToken 的有效期
                        .accessTokenTimeToLive(Duration.ofHours(12L))
                        // refreshToken 的有效期
                        .refreshTokenTimeToLive(Duration.ofHours(12L))
                        // 是否可重用刷新令牌
                        .reuseRefreshTokens(true)
                        .build()
                )
                // 是否需要用户确认一下客户端需要获取用户的哪些权限
                // 比如：客户端需要获取用户的 用户信息、用户照片 但是此处用户可以控制只给客户端授权获取 用户信息。
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
                        .build()
                )
                .build();

        *//* 每次都会初始化 生产的话 只初始化 JdbcRegisteredClientRepository *//*
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        registeredClientRepository.save(registeredClient);

        return registeredClientRepository;
        *//*return new InMemoryRegisteredClientRepository(registeredClient);*//*
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }*/

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = JwksUtil.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * 配置 OAuth2.0 provider元信息
     * @param port
     * @return
     */
    @Bean
    public ProviderSettings providerSettings(@Value("${server.port}") Integer port) {
        return ProviderSettings.builder()
                // 配置获取token的端点路径(当前是默认的，可以更改)
                .tokenEndpoint("/oauth2/token")
                // 配置获取code的端点路径(当前是默认的，可以更改)
                .authorizationEndpoint("/oauth2/authorize")
                // 配置查看code的端点路径(当前是默认的，可以更改)
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                // 配置查看jwk(公钥)的端点路径(当前是默认的，可以更改)
                .jwkSetEndpoint("/oauth2/jwks")
                .issuer("http://localhost:" + port)
                .build();
    }

    /**
     * request -> xToken 注入请求转换器
     * webflux
     * @see org.springframework.security.web.server.authentication.ServerAuthenticationConverter
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2ClientCredentialsAuthenticationConverter(),//client_credentials
                new OAuth2PasswordAuthenticationConverter(),//password
                new OAuth2SmsAuthenticationConverter()//sms
                ));
    }

    /**
     * 注入授权模式实现提供方
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {

        OAuth2TokenGenerator<Jwt> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2PasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2PasswordAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        OAuth2SmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2SmsAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new UserDetailsAuthenticationProvider());

        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken s
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }

    /**
     * 令牌生成规则实现 </br>
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {

        /*CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();*/
        /*accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());*/

        JwtGenerator JwtGenerator = new JwtGenerator(jwtEncoder);
        /* 注入Token 增加关联用户信息 */
        JwtGenerator.setJwtCustomizer(new CustomeOAuth2JwtTokenCustomizer());

        return new DelegatingOAuth2TokenGenerator(JwtGenerator, new OAuth2RefreshTokenGenerator());
    }

}
