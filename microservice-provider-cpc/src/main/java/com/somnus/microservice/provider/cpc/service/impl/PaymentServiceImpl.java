package com.somnus.microservice.provider.cpc.service.impl;

import com.somnus.microservice.cache.annotation.CachePut;
import com.somnus.microservice.cache.annotation.Cacheable;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.lock.annotation.Lock;
import com.somnus.microservice.provider.cpc.mapper.PaymentOrderMapper;
import com.somnus.microservice.provider.cpc.model.domain.PaymentOrder;
import com.somnus.microservice.provider.cpc.model.vo.PaymentOrderVo;
import com.somnus.microservice.provider.cpc.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author kevin.liu
 * @title: PaymentServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/24 19:37
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends BaseService<PaymentOrder> implements PaymentService {

    private final PaymentOrderMapper mapper;

    @Override
    @Cacheable(name = "paymentOrder", key = "#id", expire = -1L)
    public PaymentOrderVo detail(String id) {
        log.info("查询订单：{}", id);
        return new PaymentOrderVo(id, BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP), new Date());
    }

    @Override
    @CachePut(name = "paymentOrder", key = "#id", expire = -1L)
    public PaymentOrderVo order(String id) {
        log.info("提交订单：{}", id);
        return new PaymentOrderVo(id, new BigDecimal("10.05"), new Date());
    }

    /**
     * key
     * @param userId
     * @param orderId
     * 锁粒度可控，可以通过SPEL表达式设置
     * @return
     */
    @Override
    @Lock(name = "lock", key = "#userId", leaseTime = 5000L, waitTime = 60000L, async = false, fair = false)
    public String pay(String userId, String orderId) {
        try {
            log.info("支付订单 {}：{}", userId ,orderId);
            TimeUnit.MILLISECONDS.sleep(500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
