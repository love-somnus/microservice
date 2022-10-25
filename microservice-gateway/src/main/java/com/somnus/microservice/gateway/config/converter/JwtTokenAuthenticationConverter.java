package com.somnus.microservice.gateway.config.converter;

import com.somnus.microservice.commons.base.utils.JwksUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author kevin.liu
 * @title: JwtTokenAuthenticationConverter
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/23 13:37
 */
@Slf4j
public class JwtTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private String principalClaimName = JwtClaimNames.SUB;

    private final OAuth2TokenValidator<Jwt> jwtValidator = JwtValidators.createDefault();

    @Setter
    private RedisTemplate<String, Object> redisTemplate;

    private String buildKey(String appAbbr, String id) {
        return String.format("%s:%s:%s:%s", OAuth2ParameterNames.TOKEN, OAuth2ParameterNames.ACCESS_TOKEN, appAbbr, id);
    }

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        Map<String, Object> map = jwt.getClaimAsMap("user_info");

        OAuth2Authorization authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(map.get("appAbbr").toString(), map.get("uId").toString()));

        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);

        if(! jwt.getTokenValue().equals(Objects.requireNonNull(authorization).getAccessToken().getToken().getTokenValue())){
            String tokenValue = JwksUtil.generateToken(Collections.singletonMap("sub", principalClaimValue),"jwt", 8L, ChronoUnit.HOURS);
            Jwt oldJwt = Jwt.withTokenValue(tokenValue).expiresAt(Instant.now().minusSeconds(60 * 60)).header("alg","RS256").claim("calim", "calim").build();
            return new JwtAuthenticationToken(validateJwt(oldJwt), authorities, principalClaimValue);
        }

        return new JwtAuthenticationToken(jwt, authorities, principalClaimValue);

    }

    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        return this.jwtGrantedAuthoritiesConverter.convert(jwt);
    }

    public void setJwtGrantedAuthoritiesConverter(
            Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    @Deprecated
    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }

    private Jwt validateJwt(Jwt jwt) {
        OAuth2TokenValidatorResult result = this.jwtValidator.validate(jwt);
        if (result.hasErrors()) {
            Collection<OAuth2Error> errors = result.getErrors();
            String validationErrorString = getJwtValidationExceptionMessage(errors);
            throw new JwtValidationException(validationErrorString, errors);
        }
        return jwt;
    }

    private String getJwtValidationExceptionMessage(Collection<OAuth2Error> errors) {
        for (OAuth2Error oAuth2Error : errors) {
            if (!StringUtils.hasText(oAuth2Error.getDescription())) {
                return oAuth2Error.getDescription();
            }
        }
        return "Unable to validate Jwt";
    }
}
