package com.somnus.microservice.provider.cpc.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.provider.cpc.model.vo.PaymentOrderVo;
import com.somnus.microservice.provider.cpc.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Tag(name = "PaymentController", description = "支付相关接口")
public class PaymentController {

    private final PaymentService service;

    @GetMapping(value = "order/detail/{orderId}")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true, example = "1", in = ParameterIn.PATH)})
    @Operation(method = "GET", summary = "订单详情", description  = "订单详情")
    public Mono<?> detail(@PathVariable("orderId")String orderId){

        PaymentOrderVo paymentOrder = service.detail(orderId);

        return Mono.just(WrapMapper.success(paymentOrder));
    }

    @PostMapping(value = "order/{orderId}")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true, example = "1", in = ParameterIn.PATH)})
    @Operation(method = "POST", summary = "下单", description = "下单")
    public Mono<?> order(@PathVariable("orderId")String orderId){

        PaymentOrderVo paymentOrder = service.order(orderId);

        return Mono.just(WrapMapper.success(paymentOrder));
    }

    /**
     * @RequestParam只能接收get请求的参数，@RequestBody才能接收post请求的参数
     * @param request
     * @return
     */
    @PostMapping(value = "order/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    @Parameters({
            @Parameter(name = "userId", description = "用户ID", required = true, example = "1"),
            @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")})
    @Operation(method = "POST", summary = "支付订单", description = "支付订单")
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
