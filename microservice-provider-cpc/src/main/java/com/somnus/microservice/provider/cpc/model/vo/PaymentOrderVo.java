package com.somnus.microservice.provider.cpc.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kevin.liu
 * @title: PaymentOrderVo
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/27 17:45
 */
@Data
@ApiModel("订单")
@NoArgsConstructor
public class PaymentOrderVo implements Serializable {

    private static final long serialVersionUID = 5658306339591750350L;

    public PaymentOrderVo(String orderId, BigDecimal amount, Date createdTime) {
        this.orderId = orderId;
        this.amount = amount;
        this.createdTime = createdTime;
    }

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 游戏简称
     */
    private String gameAbbr;

    /**
     * 游戏全称
     */
    private String gameName;

    /**
     * 商品ID
     */
    private String goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 币种
     */
    private String currency;

    /**
     * 商品个数
     */
    private Integer goodsCount;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 用户id
     */
    private String boltrendId;

    /**
     * 角色名称
     */
    private String playerName;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 订单状态(0:待支付|1:支付成功|2:支付失败)
     */
    private Integer status;

    /**
     * 支付通道
     */
    private Integer payChannel;

    /**
     * 支付通道名称
     */
    private String payChannelName;

    /**
     * 订单创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 订单支付成功时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;
}
