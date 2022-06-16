package com.somnus.microservice.commons.security.core.constant;

/**
 * @author kevin.liu
 * @title: SecurityConstants
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 13:12
 */
public interface SecurityConstants {

    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";

    /**
     * 前缀
     */
    String PROJECT_PREFIX = "pig";

    /**
     * 项目的license
     */
    String PROJECT_LICENSE = "https://github.com/love-somnus";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * 默认登录URL
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";

    /**
     * grant_type
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 手机号登录
     */
    String SMS = "sms";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * {noop} 加密的特征码
     */
    String NOOP = "{noop}";

    /***
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 用户信息
     */
    String DETAILS_USER = "user_info";

    /**
     * 协议字段
     */
    String DETAILS_LICENSE = "license";

    /**
     * 验证码有效期,默认 60秒
     */
    long CODE_TIME = 60;

    /**
     * 验证码长度
     */
    String CODE_SIZE = "6";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 客户端ID
     */
    String CLIENT_ID = "clientId";

    /**
     * 短信登录 参数名称
     */
    String SMS_PARAMETER_NAME = "mobile";

    /**
     * 授权码模式confirm
     */
    String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    /**
     * 删除
     */
    String STATUS_DEL = "1";

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

}
