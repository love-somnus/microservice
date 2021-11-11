package com.somnus.microservice.commons.core.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.request
 * @title: MessageBody
 * @description: TODO
 * @date 2020/12/10 13:08
 */
@Data
public class MessageBody implements Serializable {

    private Long id;

    /** 消息内容 */
    private String content;

    /** 发送消息的用户 */
    private String sender;

    /** 目标用户（告知 STOMP 代理转发到哪个用户） */
    private String receiver;

    /** 目标用户（告知 STOMP 代理转发到哪个用户） */
    private Integer appId;

    public MessageBody wrap(Long id){
        this.id = id;
        return this;
    }

}
