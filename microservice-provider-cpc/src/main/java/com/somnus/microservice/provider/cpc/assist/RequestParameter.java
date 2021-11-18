package com.somnus.microservice.provider.cpc.assist;

import com.somnus.microservice.commons.base.enums.PayChannel;
import com.somnus.microservice.commons.base.enums.RequestType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kevin.liu
 * @title: RequestParameter
 * @projectName webteam
 * @description: 支付渠道回调参数
 * @date 2021/1/25 16:54
 */
@Data
@NoArgsConstructor
public class RequestParameter<P, R> implements Serializable {

    private static final long serialVersionUID = 2381981255029397408L;

    private PayChannel channel;

    private RequestType type;

    private P				parameter;

    private R				result;

    public RequestParameter(PayChannel channel, RequestType type, P parameter){
        this.channel = channel;
        this.type = type;
        this.parameter = parameter;
    }
}
