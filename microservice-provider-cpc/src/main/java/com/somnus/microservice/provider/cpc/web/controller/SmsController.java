package com.somnus.microservice.provider.cpc.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.limit.annotation.Limit;
import com.somnus.microservice.provider.cpc.model.body.SmsCodeRequest;
import com.somnus.microservice.provider.cpc.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

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
@Tag(name = "SmsController", description = "短信相关接口")
public class SmsController {

    @Autowired
    private SmsService service;

    /**
     * @param request
     * @return
     */
    @PostMapping(value = "send-sms-code", produces = MediaType.APPLICATION_JSON_VALUE)
    @Parameters({
            @Parameter(name = "mobile", description = "手机号", required = true, example = "1"),
            @Parameter(name = "validateCode", description = "校验码", required = true, example = "1")})
    @Operation(method = "POST", requestBody = @RequestBody(content = @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)),
            summary = "发送短信验证码", description = "60秒最多允许发送3次【限流】")
    @Limit(name = "limit", key = "#request.mobile", rate = 3, rateInterval = 60, rateIntervalUnit = TimeUnit.SECONDS)
    public Mono<?> sendSmsCode(SmsCodeRequest request){

        String smsCode = service.sendSmsCode(request.getMobile(), request.getValidateCode());

        return Mono.just(WrapMapper.success(smsCode));
    }

}
