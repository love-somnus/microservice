package com.somnus.microservice.provider.cpc.api.service;

import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.provider.cpc.api.service.hystrix.AuthenticateFeignHystrix;
import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/*@FeignClient(value = "cpc", fallback = AuthenticateFeignHystrix.class)*/
@ReactiveFeignClient(name = "cpc", fallback = AuthenticateFeignHystrix.class)
public interface AuthenticateFeignApi {

    @GetMapping(value = "/api/oauth2/query")
    Mono<Wrapper<?>> query();
}
