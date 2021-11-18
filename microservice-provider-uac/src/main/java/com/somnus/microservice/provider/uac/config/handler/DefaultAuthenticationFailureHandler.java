package com.somnus.microservice.provider.uac.config.handler;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: DefaultAuthenticationFailureHandler
 * @projectName uac
 * @description: 自定义登录失败Handler
 * @date 2021/11/15 15:09
 */
@Component
public class DefaultAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange()
                .getResponse()).flatMap(response -> {
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    Wrapper<?> wrapper = WrapMapper.error();
                    // 账号不存在
                    if (exception instanceof UsernameNotFoundException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.ACCOUNT_NOT_EXIST);
                        // 用户名或密码错误
                    } else if (exception instanceof BadCredentialsException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.LOGIN_PASSWORD_ERROR);
                        // 账号已过期
                    } else if (exception instanceof AccountExpiredException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.ACCOUNT_EXPIRED);
                        // 账号已被锁定
                    } else if (exception instanceof LockedException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.ACCOUNT_LOCKED);
                        // 用户凭证已失效
                    } else if (exception instanceof CredentialsExpiredException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.ACCOUNT_CREDENTIAL_EXPIRED);
                        // 账号已被禁用
                    } else if (exception instanceof DisabledException) {
                        wrapper = WrapMapper.fail(ErrorCodeEnum.ACCOUNT_DISABLE);
                    }
                    DataBuffer dataBuffer = dataBufferFactory.wrap(JacksonUtil.toJson(wrapper).getBytes());
                    return response.writeWith(Mono.just(dataBuffer));
                })
       );
    }
}
