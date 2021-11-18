package com.somnus.microservice.provider.cpc.assist.handler;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.enums.RequestType;
import com.somnus.microservice.provider.cpc.api.exception.CpcBizException;
import com.somnus.microservice.provider.cpc.assist.RequestParameter;
import com.somnus.microservice.provider.cpc.model.domain.PaymentOrder;
import com.somnus.microservice.provider.cpc.model.vo.PaymentResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author kevin.liu
 * @title: AbstractPaymentHandler
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/25 17:29
 */
@Slf4j
public abstract class AbstractPaymentHandler {

    private static ThreadLocal<RequestParameter<?, ?>> parameterHolder = new ThreadLocal<>();

    /**
     * 处理支付相关的请求
     * @param parameter
     * @param <P>
     * @param <R>
     * @return
     */
    public <P, R> R handle(RequestParameter<P, R> parameter){
        log.info("处理[{}]渠道的[{}]请求:[{}]", new Object[]{parameter.getChannel(), parameter.getType(), parameter.getParameter()});
        parameterHolder.set(parameter);
        RequestType type = parameter.getType();
        Object result;
        switch (type) {
            case NOTIFY:
                result = this.handleNotify((RequestParameter<P, String>) parameter);
                break;
            case ORDER:
                result = this.handleOrder((RequestParameter<PaymentOrder, String>) parameter);
                break;
            case QUERY:
                result = this.handleQuery((RequestParameter<String, Map<String, String>>) parameter);
                break;
            case CONFIRM:
                result = this.handleConfirm((RequestParameter<String, PaymentResult>) parameter);
                break;
            default:
                throw new CpcBizException(ErrorCodeEnum.EN20005);
        }
        parameterHolder.remove();
        return (R) result;
    }

    protected abstract <P> String handleNotify(RequestParameter<P, String> parameter);

    /**
     * 构建第三方支付页面跳转表单
     * @param parameter 请求参数
     * @return 跳转表单
     */
    protected abstract String handleOrder(RequestParameter<PaymentOrder, String> parameter);

    /**
     * 根据订单号查询第三方支付结果
     * @param parameter 请求参数
     * @return 第三方支付结果
     */
    protected abstract Map<String, String> handleQuery(RequestParameter<String, Map<String, String>> parameter);

    /**
     * 根据订单号确认支付是否成功
     * @param parameter 请求参数
     * @return 支付是否成功
     */
    protected PaymentResult handleConfirm(RequestParameter<String, PaymentResult> parameter){
        log.warn("当前渠道不支持主动确认支付结果");
        return parameter.getResult();
    }
}
