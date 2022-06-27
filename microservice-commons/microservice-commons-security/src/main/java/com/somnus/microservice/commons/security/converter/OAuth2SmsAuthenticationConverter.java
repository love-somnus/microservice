package com.somnus.microservice.commons.security.converter;

import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.util.OAuth2EndpointUtils;
import com.somnus.microservice.commons.security.token.OAuth2SmsAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
/**
 * <p>短信登录转换器</p>
 * @author kevin
 * @title: OAuth2SmsAuthenticationConverter
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 13:50
 */
public class OAuth2SmsAuthenticationConverter extends OAuth2BaseAuthenticationConverter<OAuth2SmsAuthenticationToken> {

    /**
     * 是否支持此convert
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.SMS.equals(grantType);
    }

    @Override
    public OAuth2SmsAuthenticationToken buildToken(Authentication clientPrincipal, Set requestedScopes, Map additionalParameters) {
        return new OAuth2SmsAuthenticationToken(new AuthorizationGrantType(SecurityConstants.SMS), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数 密码模式密码必须不为空
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // PHONE (REQUIRED)
        String mobile = parameters.getFirst(SecurityConstants.SMS_PARAMETER_NAME);
        if (!StringUtils.hasText(mobile) || parameters.get(SecurityConstants.SMS_PARAMETER_NAME).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.SMS_PARAMETER_NAME, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

}