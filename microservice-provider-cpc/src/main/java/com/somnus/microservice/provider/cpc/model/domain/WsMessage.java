package com.somnus.microservice.provider.cpc.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.somnus.microservice.commons.core.mybatis.BaseEntity;
import com.somnus.microservice.provider.cpc.model.enums.WsMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;

/**
 * @author kevin.liu
 * @date 2022/9/28 19:45
 */
@Data
@Table(name = "ws_message")
@Alias(value = "WsMessage")
@EqualsAndHashCode(callSuper = true)
public class WsMessage extends BaseEntity {

    public WsMessage(){
        super.creator();
    }

    public WsMessage(String sender, String receiver, String content) {
        super.creator();
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    /**
     * 发送人
     */
    @TableField(value = "sender")
    private String sender;

    /**
     * 接收人
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 消息状态(0:消息未确认|1:消息已确认)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 消息类型(0:在线消息|离线消息)
     */
    @TableField(value = "type")
    private Integer type;

    public WsMessage wrap(WsMessageType type){

        this.type = type.getType();

        return this;
    }

}
