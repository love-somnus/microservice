package com.somnus.microservice.provider.cpc.assist.handler.payment;

import com.somnus.microservice.commons.base.enums.HandlerType;
import com.somnus.microservice.provider.cpc.assist.RequestParameter;
import com.somnus.microservice.provider.cpc.assist.handler.AbstractPaymentHandler;
import com.somnus.microservice.provider.cpc.model.domain.PaymentOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author kevin.liu
 * @title: PaypalPaymentHandler
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/25 17:31
 */
@Slf4j
@Component
@RefreshScope
@HandlerType(values = {"paypal"})
public class PaypalPaymentHandler extends AbstractPaymentHandler {

    @Override
    protected <P> String handleNotify(RequestParameter<P, String> parameter) {
        return null;
    }

    @Override
    protected String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
        System.out.println("paypal handleOrder");
        return "https://www.paypal.com/";
    }

    @Override
    protected Map<String, String> handleQuery(RequestParameter<String, Map<String, String>> parameter) {
        return null;
    }
}
