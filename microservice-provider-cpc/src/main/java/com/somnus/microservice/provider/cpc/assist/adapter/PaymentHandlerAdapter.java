package com.somnus.microservice.provider.cpc.assist.adapter;

import com.somnus.microservice.commons.base.enums.PayChannel;
import com.somnus.microservice.commons.core.support.SpringContextHolder;
import com.somnus.microservice.provider.cpc.assist.handler.AbstractPaymentHandler;

import java.util.Map;
import java.util.Optional;

/**
 * @author kevin.liu
 * @title: PaymentHandlerAdapter
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/25 17:25
 */
public class PaymentHandlerAdapter {

    private final Map<String, Class<?>> handlerMap;

    public PaymentHandlerAdapter(Map<String, Class<?>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public AbstractPaymentHandler getHandler(PayChannel payChannel) {

        Class<?> clazz = handlerMap.get(payChannel.getChannel());

        Optional.ofNullable(clazz).orElseThrow(() -> new IllegalArgumentException("not found PaymentHandlerAdapter for payChannel: " + payChannel));

        return (AbstractPaymentHandler) SpringContextHolder.getBean(clazz);
    }


}
