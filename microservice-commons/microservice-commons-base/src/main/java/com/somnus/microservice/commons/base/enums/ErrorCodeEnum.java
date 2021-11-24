package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: ErrorCodeEnum
 * @description: The class Error code enum.
 * @date 2019/3/15 15:55
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCodeEnum {

    /**
     * 用户账号操作异常
     */
    LOGIN_PASSWORD_ERROR(5000,"账号或密码错误"),

    ACCOUNT_EXPIRED(5001,"账号已过期"),

    ACCOUNT_LOCKED(5002,"账号已被锁定"),

    ACCOUNT_CREDENTIAL_EXPIRED(5003,"账号凭证已过期"),

    ACCOUNT_DISABLE(5004,"账号已被禁用"),

    PERMISSION_DENIED(5005,"账号没有权限"),

    USER_UNAUTHORIZED(5006,"账号未认证"),

    ACCOUNT_NOT_EXIST(5007,"账号不存在"),

    // token 异常
    TOKEN_ERROR(5008,"非法token异常"),

    TOKEN_EXPIRED(5009,"token 过期"),

    //EN 10001 英文（需要前端翻译）
    //CN 10001 中文（后端日志，不需要翻译）
    /********************************** EN code 10001-19999 英文（需要前端翻译, 针对网站使用） start *********************************/
    EN10000(10000, "invalid"),//格式不合法

    EN10001(10001, "noPermission"),//没有权限

    EN10002(10002, "secondVerifyFail"),//二次校验失败

    EN10005(10005, "booked"),//已订阅

    EN10006(10006, "smsCodeError"), //短信验证码错误

    EN10009(10009, "unSignup"),//未注册

    EN10010(10010, "signuped"),//已注册

    EN10011(10011, "outnumber"),//超员

    EN10014(10014, "infoRepeat"),//信息重复

    EN10015(10015, "frequently"),//频繁调用

    EN10016(10016, "unseasonal"),

    EN10017(10017, "full"),

    EN10018(10018, "undo"),//已撤销

    EN10019(10019, "unlogin"), //未登录

    EN10020(10020, "mismatch"),//未匹配

    EN10021(10021, "repeat"),//重复

    EN10026(10026, "isNotNull"),//不能为空

    EN10027(10027, "notfound"),//纪录未找到

    EN10030(10030, "forbidden"),//禁止

    EN10031(10031, "delete"),//已删除

    EN10034(10034, "exist"),

    EN10036(10036, "limit"),

    EN10039(10039, "timedOut"),//请求超时

    EN20001(20001, "unStart"),//未开始

    EN20002(20002, "end"),//已结束

    EN20003(20003, "illegal"),//非法操作

    EN20004(20004, "none"),//可用于游戏不存在

    EN20005(20005, "unsupported"),//不支持的操作

    EN20006(20006, "overdue"),//已过期

    EN20007(20007, "paid"),//已付款

    /********************************** CN code 10001-19999 中文（后端日志，不需要翻译, 主要是针对CMS管理后台使用） start *********************************/
    CN10000(10000, "解析header失败"),

    CN20000(20000, "发送短信失败，原因为: {0}"),

    CN10001(10001, "请求未鉴权"),

    CN99999(99999, "message 第三方接口可以使用改错误码，返回错误信息"),
    ;


    @Getter
    private final int code;

    @Getter
    private final String msg;

    /**
     * Gets enum.
     *
     * @param code the code
     *
     * @return the enum
     */
    public static ErrorCodeEnum codeOf(int code) {
        for (ErrorCodeEnum ele : ErrorCodeEnum.values()) {
            if (ele.getCode() == code) {
                return ele;
            }
        }
        return null;
    }
}