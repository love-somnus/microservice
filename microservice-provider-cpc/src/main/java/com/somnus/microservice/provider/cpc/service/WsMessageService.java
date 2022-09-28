package com.somnus.microservice.provider.cpc.service;

import com.somnus.microservice.commons.base.request.MessageBody;
import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.provider.cpc.model.domain.WsMessage;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:44
 */
public interface WsMessageService extends IService<WsMessage> {

    /**
     * 点对点个人发送消息
     * @param messageBody
     */
    void p2p(MessageBody messageBody);

}