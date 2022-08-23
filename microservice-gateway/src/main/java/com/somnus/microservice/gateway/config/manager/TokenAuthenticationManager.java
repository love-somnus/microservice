package com.somnus.microservice.gateway.config.manager;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: TokenAuthenticationManager
 * @projectName uac
 * @description: token 认证处理
 *               ReactiveAuthenticationManager用于封装JWT为OAuth2Authentication并判断Token的有效性
 * @date 2021/11/15 15:26
 */
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveJwtDecoder jwtDecoder;

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverterAdapter(
            new JwtAuthenticationConverter());

    public TokenAuthenticationManager(ReactiveJwtDecoder jwtDecoder) {
        Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // @formatter:off
        return Mono.justOrEmpty(authentication)
                .filter((a) -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap(this.jwtDecoder::decode)
                .flatMap(this.jwtAuthenticationConverter::convert)
                .cast(Authentication.class)
                .onErrorMap(JwtException.class, this::onError);
        // @formatter:on
    }

    /**
     * Use the given {@link Converter} for converting a {@link Jwt} into an
     * {@link AbstractAuthenticationToken}.
     * @param jwtAuthenticationConverter the {@link Converter} to use
     */
    public void setJwtAuthenticationConverter(
            Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        Assert.notNull(jwtAuthenticationConverter, "jwtAuthenticationConverter cannot be null");
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    private AuthenticationException onError(JwtException ex) {
        if (ex instanceof BadJwtException) {
            return new InvalidBearerTokenException(ex.getMessage(), ex);
        }
        return new AuthenticationServiceException(ex.getMessage(), ex);
    }
}
