package com.somnus.microservice.provider.cpc.service;

import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.provider.cpc.model.domain.PaymentOrder;
import com.somnus.microservice.provider.cpc.model.vo.PaymentOrderVo;

public interface PaymentService extends IService<PaymentOrder> {

    /**
     * 订单详情
     * @param id
     * @return
     */
    PaymentOrderVo detail(String id);

    /**
     * 下单
     * @param id
     * @return
     */
    PaymentOrderVo order(String id);

    /**
     * 支付订单
     * @param orderId
     * @return
     */
    String pay(String userId, String orderId);
}
