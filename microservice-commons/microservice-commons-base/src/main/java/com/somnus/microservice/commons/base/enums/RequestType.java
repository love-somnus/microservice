package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RequestType {

    RETURN(1, "前端回调"), NOTIFY(2, "后端回调"),

    ORDER(3, "支付建单"), REFUND(4, "退款"),

    QUERY(5, "结果查询"), CONFIRM(6, "结果确认");

    @Getter
    private Integer value;

    @Getter
    private String desc;


    public static RequestType nameOf(String name){
        RequestType type = null;
        if (name != null){
            for (RequestType t : RequestType.values()) {
                if (t.name().equalsIgnoreCase(name)){
                    type = t;
                }
            }
        }
        return type;
    }
}
