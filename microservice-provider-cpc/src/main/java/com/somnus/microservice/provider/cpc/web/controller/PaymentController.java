package com.somnus.microservice.provider.cpc.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.provider.cpc.model.vo.PaymentOrderVo;
import com.somnus.microservice.provider.cpc.service.PaymentService;
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
 * @title: PaymentController
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/24 19:49
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping(value = "payment")
@Api(value = "Web - PaymentController")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping(value = "order/detail/{orderId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "String", example = "1", paramType = "path")})
    @ApiOperation(httpMethod = "GET", value = "订单详情", notes = "订单详情")
    public Mono<?> detail(@PathVariable("orderId")String orderId){

        PaymentOrderVo paymentOrder = service.detail(orderId);

        return Mono.just(WrapMapper.success(paymentOrder));
    }

    @PostMapping(value = "order/{orderId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "String", example = "1", paramType = "path")})
    @ApiOperation(httpMethod = "POST", value = "下单", notes = "下单")
    public Mono<?> order(@PathVariable("orderId")String orderId){

        PaymentOrderVo paymentOrder = service.order(orderId);

        return Mono.just(WrapMapper.success(paymentOrder));
    }

    @PostMapping(value = "order/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", example = "1"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = true, dataType = "String", example = "1")})
    @ApiOperation(httpMethod = "POST", value = "支付订单", notes = "支付订单")
    public Mono<?> pay(@RequestBody PayOrderRequest request){

        String url = service.pay(request.getUserId(), request.getOrderId());

        return Mono.just(WrapMapper.success(url));
    }

    @Data
    private static class PayOrderRequest{

        private String userId;

        private String orderId;
    }
}
