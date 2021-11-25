package com.somnus.microservice.provider.cpc.service.impl;

import com.somnus.microservice.cache.annotation.CachePut;
import com.somnus.microservice.cache.annotation.Cacheable;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.provider.cpc.mapper.PaymentOrderMapper;
import com.somnus.microservice.provider.cpc.model.domain.PaymentOrder;
import com.somnus.microservice.provider.cpc.model.vo.PaymentOrderVo;
import com.somnus.microservice.provider.cpc.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kevin.liu
 * @title: PaymentServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/24 19:37
 */
@Slf4j
@Service
public class PaymentServiceImpl extends BaseService<PaymentOrder> implements PaymentService {

    @Autowired
    private PaymentOrderMapper mapper;

    @Override
    @Cacheable(name = "paymentOrder", key = "#id", expire = -1L)
    public PaymentOrderVo detail(String id) {
        log.info("查询订单：{}", id);
        return new PaymentOrderVo(id, BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP), new Date());
    }

    @Override
    @CachePut(name = "paymentOrder", key = "#id", expire = -1L)
    public PaymentOrderVo submit(String id) {
        log.info("提交订单：{}", id);
        return new PaymentOrderVo(id, BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP), new Date());
    }
}
