package com.somnus.microservice.provider.oauth2.web.rpc;

import com.somnus.microservice.provider.oauth.api.exception.Oauth2BizException;
import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.provider.oauth2.api.service.AuthenticateFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: AuthorizationFeignClient
 * @projectName github
 * @description: TODO
 * @date 2021/10/31 16:19
 */
@Slf4j
@RestController
public class AuthenticateFeignClient implements AuthenticateFeignApi {

    @Override
    public Mono<Wrapper<?>> query() {
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
        } catch (Oauth2BizException ex) {
            log.error("RPC查询, 出现异常={}", ex.getMessage(), ex);
            return Mono.just(WrapMapper.fail(ex.getMessage()));
        } catch (Exception e) {
            log.error("RPC查询, 出现异常={}", e.getMessage(), e);
            return Mono.just(WrapMapper.error());
        }
        return Mono.just(WrapMapper.ok());
    }
}
