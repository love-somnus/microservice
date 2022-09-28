package com.somnus.microservice.provider.cpc.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:47
 */
@RequiredArgsConstructor
public enum WsMessageType {

    /**
     * 消息类型
     */
    ONLINE(0, "在线消息"),

    OFFLINE(1, "离线消息");

    @Getter
    private final Integer type;

    @Getter
    private final String desc;

}
