package com.somnus.microservice.provider.cpc.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.limit.annotation.Limit;
import com.somnus.microservice.provider.cpc.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author kevin.liu
 * @title: SmsController
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/24 19:49
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping(value = "sms")
@Api(value = "Web - SmsController")
public class SmsController {

    @Autowired
    private SmsService service;

    /**
     * @param request
     * @return
     */
    @PostMapping(value = "send-sms-code", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", example = "1"),
            @ApiImplicitParam(name = "validateCode", value = "校验码", required = true, dataType = "String", example = "1")})
    @ApiOperation(httpMethod = "POST", value = "发送短信验证码", notes = "1分钟最多允许发送3次【限流】")
    @Limit(name = "limit", key = "#request.mobile", limitPeriod = 60, limitCount = 3)
    public Mono<?> sendSmsCode(@RequestBody SmsCodeRequest request){

        String smsCode = service.sendSmsCode(request.getMobile(), request.getValidateCode());

        return Mono.just(WrapMapper.success(smsCode));
    }

    @Data
    private static class SmsCodeRequest{

        private String mobile;

        private String validateCode;
    }
}
