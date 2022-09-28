package com.somnus.microservice.provider.uac.web.controller;

import com.somnus.microservice.provider.cpc.api.service.AuthenticateFeignApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: AuthenticateController
 * @projectName github
 * @description: TODO
 * @date 2021/10/31 18:33
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "AuthenticateController", description = "测试相关接口")
public class AuthenticateController {

    private final AuthenticateFeignApi feignApi;

    @GetMapping(value = "query")
    public Mono<?> query(){
        return feignApi.query();
    }
}
