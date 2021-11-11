package com.somnus.microservice.provider.oauth2.api.service.hystrix;

import com.somnus.microservice.commons.utils.wrapper.WrapMapper;
import com.somnus.microservice.commons.utils.wrapper.Wrapper;
import com.somnus.microservice.provider.oauth2.api.service.AuthenticateFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: AuthorizationFeignHystrix
 * @projectName github
 * @description: TODO
 * @date 2021/10/31 16:16
 */
@Slf4j
@Component
public class AuthenticateFeignHystrix implements AuthenticateFeignApi {

    @Override
    public Mono<Wrapper<?>> query() {
        return Mono.just(WrapMapper.waiting());
    }
}
