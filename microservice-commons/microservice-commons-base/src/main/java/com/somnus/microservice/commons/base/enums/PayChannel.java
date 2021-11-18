package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PayChannel {

    GashPay(14, "gash", "GASH"),

    PaypalPay(15, "Paypal", "美银宝"),

    AliPay(16, "ali", "支付宝");

    @Getter
    private Integer value;

    @Getter
    private String channel;

    @Getter
    private String desc;

    public static PayChannel valueOf(Integer value){
        PayChannel channel = null;
        if (value != null){
            for (PayChannel t : PayChannel.values()) {
                if (t.getValue().equals(value)){
                    channel = t;
                }
            }
        }
        return channel;
    }
}
