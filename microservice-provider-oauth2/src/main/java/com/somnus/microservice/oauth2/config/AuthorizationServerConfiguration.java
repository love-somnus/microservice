package com.somnus.microservice.oauth2.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.somnus.microservice.commons.base.utils.JwksUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * @author kevin.liu
 * @title: AuthorizationServerConfiguration
 * @projectName oauth2
 * @description: http://192.168.97.101:8002/oauth2/token?client_id=micro-client&client_secret=micro-secret&grant_type=client_credentials
 * @date 2021/11/16 13:55
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        // TODO 你可以根据需求对authorizationServerConfigurer进行一些个性化配置
        RequestMatcher authorizationServerEndpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        // 拦截 授权服务器相关的请求端点
        http.requestMatcher(authorizationServerEndpointsMatcher)
                .authorizeRequests().anyRequest().authenticated().and()
                // 忽略掉相关端点的csrf
                .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerEndpointsMatcher))
                // 开启form登录
                .formLogin()
                .and()
                // 应用 授权服务器的配置
                .apply(authorizationServerConfigurer);

        return http.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                /* 客户端ID和密码 */
                .clientId("micro-client")
                .clientSecret("micro-secret")
                /* 授权方法 */
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                /* 授权类型 */
                .authorizationGrantTypes(authorizationGrantTypes ->
                        authorizationGrantTypes.addAll(Arrays.asList(
                                AuthorizationGrantType.AUTHORIZATION_CODE,
                                AuthorizationGrantType.REFRESH_TOKEN,
                                AuthorizationGrantType.CLIENT_CREDENTIALS)))
                /* 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名  不能使用 localhost */
                .redirectUris(redirectUris ->
                        redirectUris.addAll(Collections.singletonList(
                                "http://192.168.97.101:8002/authorized"
                        )))
                .scopes(scopes -> scopes.addAll(Arrays.asList(OidcScopes.OPENID, "message.read", "message.write")))
                /* JWT的配置项 包括 TTL  是否复用refreshToken等等 */
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(60L)).build())
                /* 配置客户端相关的配置项，包括验证密钥或者 是否需要授权页面 */
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        /* 每次都会初始化 生产的话 只初始化 JdbcRegisteredClientRepository */
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        registeredClientRepository.save(registeredClient);

        return registeredClientRepository;
        /*return new InMemoryRegisteredClientRepository(registeredClient);*/
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

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

    /**
     * 配置 OAuth2.0 provider元信息
     * @param port
     * @return
     */
    @Bean
    public ProviderSettings providerSettings(@Value("${server.port}") Integer port) {
        return ProviderSettings.builder()
                .issuer("http://localhost:" + port)
                .build();
    }

}
