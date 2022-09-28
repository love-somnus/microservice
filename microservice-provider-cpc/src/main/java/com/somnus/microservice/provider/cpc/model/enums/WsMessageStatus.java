package com.somnus.microservice.provider.cpc.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:46
 */
@RequiredArgsConstructor
public enum WsMessageStatus {

    /**
     * 消息状态
     */
    NACK(0, "消息未确认"),

    ACK(1, "消息已确认");

    @Getter
    private final Integer status;

    @Getter
    private final String desc;

}
