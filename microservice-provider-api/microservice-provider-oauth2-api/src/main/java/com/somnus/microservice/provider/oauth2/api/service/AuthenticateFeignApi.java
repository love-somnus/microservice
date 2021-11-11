package com.somnus.microservice.provider.oauth2.api.service;

import com.somnus.microservice.commons.utils.wrapper.Wrapper;
import com.somnus.microservice.provider.oauth2.api.service.hystrix.AuthenticateFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/*@FeignClient(value = "oauth2", fallback = AuthenticateFeignHystrix.class)*/
@ReactiveFeignClient(name = "oauth2", fallback = AuthenticateFeignHystrix.class)
public interface AuthenticateFeignApi {

    @GetMapping(value = "/api/oauth2/query")
    Mono<Wrapper<?>> query();
}
