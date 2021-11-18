package com.somnus.microservice.provider.cpc.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.somnus.microservice.commons.core.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kevin.liu
 * @title: PaymentOrder
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/27 15:34
 */
@Data
@Table(name = "payment_order")
@Alias(value = "PaymentOrder")
@EqualsAndHashCode(callSuper = true)
public class PaymentOrder extends BaseEntity {

    private static final long serialVersionUID = -1934600359787282629L;

    public PaymentOrder(){
        super.creator();
    }

    /**
     * 订单编号
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 支付订单号
     */
    @Column(name = "tcd")
    private String tcd;

    /**
     * 游戏的appid
     */
    @Column(name = "appid")
    private String appid;

    /**
     * 游戏简称
     */
    @Column(name = "game_abbr")
    private String gameAbbr;

    /**
     * 游戏全称
     */
    @Column(name = "game_name")
    private String gameName;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 币种
     */
    @Column(name = "currency")
    private String currency;

    /**
     * 商品个数
     */
    @Column(name = "goods_count")
    private String goodsCount;

    /**
     * 订单总金额
     */
    @Column(name = "amount")
    private BigDecimal amount;

    /**
     * 用户id
     */
    @Column(name = "boltrend_id")
    private String boltrendId;

    /**
     * 角色名称
     */
    @Column(name = "player_name")
    private String playerName;

    /**
     * 服务器名称
     */
    @Column(name = "server_name")
    private String serverName;

    /**
     * 支付通道
     */
    @Column(name = "pay_channel")
    private Integer payChannel;

    /**
     * 支付通知地址
     */
    @Column(name = "notify_url")
    private String notifyUrl;

    /**
     * 订单状态(0:待支付|1:支付成功|2:支付失败)
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 订单支付状态：0支付成功，1支付超时，2支付取消，3支付失败，4支付处理中
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 标签(到达过第三方支付界面)
     */
    @Column(name = "tag")
    private Integer tag;

    /**
     * 订单支付时间
     */
    @Column(name = "complete_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;
}
