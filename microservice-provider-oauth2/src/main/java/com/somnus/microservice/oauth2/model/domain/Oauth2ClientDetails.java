package com.somnus.microservice.oauth2.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.somnus.microservice.commons.core.mybatis.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;

/**
 * @author kevin.liu
 * @title: Oauth2ClientDetails
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 20:31
 */
@Data
@Table(name = "oauth2_client_details")
@Alias(value = "Oauth2ClientDetails")
@EqualsAndHashCode(callSuper = true)
public class Oauth2ClientDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @TableField(value = "client_id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;

    /**
     * 资源ID
     */
    @TableField("resource_ids")
    private String resourceIds;

    /**
     * 作用域
     */
    @TableField("scopes")
    private String scopes;

    /**
     * 授权模式（refresh_token,client_credentials,password,authorization_code,sms）
     */
    @TableField("authorization_grant_types")
    private String authorizationGrantTypes;

    /**
     * 授权方法（client_secret_post,client_secret_basic）
     */
    @TableField("client_authentication_methods")
    private String clientAuthenticationMethods;

    /**
     * 回调地址
     */
    @TableField("redirect_uris")
    private String redirectUris;

    /**
     * 权限
     */
    @TableField("authorities")
    private String authorities;

    /**
     * 请求令牌有效时间
     */
    @TableField("access_token_validity")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间
     */
    @TableField("refresh_token_validity")
    private Integer refreshTokenValidity;

    /**
     * 扩展信息
     */
    @TableField("additional_information")
    private String additionalInformation;

    /**
     * 是否自动放行
     */
    @TableField("auto_approve")
    private String autoApprove;
}
