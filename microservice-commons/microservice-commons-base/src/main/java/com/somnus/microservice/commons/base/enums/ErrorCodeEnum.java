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

    EN10003(10003, "voteDayRunOut"),

    EN10004(10004, "smsCodeNotExist"),//短信验证码不存在

    EN10005(10005, "booked"),//已订阅

    EN10006(10006, "smsCodeError"), //短信验证码错误

    EN10007(10007, "voteRunOut"),

    EN10008(10008, "teamExists"),//队伍已存在

    EN10009(10009, "unSignup"),//未注册

    EN10010(10010, "signuped"),//已注册

    EN10011(10011, "outnumber"),//超员

    EN10012(10012, "activityEnd"),//活动结束

    EN10013(10013, "luckyDrawRunOut"),//抽奖次数已经用完

    EN10014(10014, "infoRepeat"),//信息重复

    EN10015(10015, "frequently"),//频繁调用

    EN10016(10016, "unseasonal"),

    EN10017(10017, "full"),

    EN10018(10018, "undo"),//已撤销

    EN10019(10019, "unlogin"), //未登录

    EN10020(10020, "mismatch"),//未匹配

    EN10021(10021, "repeat"),//重复

    EN10022(10022, "userError"),//用户信息错误

    EN10023(10023, "unConditions"),//条件未达到

    EN10024(10024, "oneself"),//不能邀请自己

    EN10025(10025, "starEnd"),//点赞结束

    EN10026(10026, "isNotNull"),//不能为空

    EN10027(10027, "notfound"),//纪录未找到

    EN10028(10028, "teamOutnumber"),//创建队伍已超过数量

    EN10029(10029, "joinOutnumber"),//加入队伍已超过数量

    EN10030(10030, "forbidden"),//禁止

    EN10031(10031, "delete"),//已删除

    EN10032(10032, "checkFailure"),//检测失败

    EN10033(10033, "pubOutnumber"),//发布超出限制

    EN10034(10034, "exist"),

    EN10035(10035, "non-existent"),

    EN10036(10036, "limit"),

    EN10037(10037, "un_code"),

    EN10038(10038, "verifyFail"),//校验失败

    EN10039(10039, "timedOut"),//请求超时

    EN10040(10040, "content is empty"),//内容不能为空

    EN10041(10041, "signature is empty"),//签名不能为空

    EN10042(10042, "header system is empty"),//header system来源为空

    EN00000(00000, "systemError"),//系统错误

    EN99999(99999, "http request error"),//前端不做翻译

    EN20000(20000, "game:[{0}] key:[{1}] not found"),//已结束

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

    CN20001(20001, "您要操作的数据，[{0}]已存在"),

    CN20003(20003, "您要操作的数据，{0}：{1} 已存在"),

    CN20004(20004, "您要操作的数据，{0}：{1}和{2}：{3} 已存在"),

    CN30001(30001, "您要操作的数据，[{0}]和[{1}]已存在"),

    CN40001(40001, "您要操作的数据，[{0}]和[{1}]和[{2}]已存在"),

    CN20002(20002, "您要操作的数据，ID[{0}]不存在"),

    CN10005(10005, "id[{0}]下面还有子类资源，不允许删除当前父资源"),

    CN10006(10006, "您要查询的数据id[{0}]不存在"),

    CN10007(10007, "栏目[{0}]下面还有新闻，不允许删除当前栏目"),

    CN10008(10008, "haveNoColumn"),

    CN10009(10009, "您无权限使用CMS系统，请联系管理员"),

    CN10013(10013, "游戏[{0}]不存在"),

    CN10044(10044, "四个keyword都已存在"),

    CN10045(10045, "目标日期[{0}]不能早于当前日期"),

    CN10048(10048, "POPO服务号配置出错，请检查"),

    CN10049(10049, "用户信息不存在"),

    CN10050(10050, "该用户类型不支持修改昵称和头像"),

    CN10051(10051, "上传文件到云存储失败，原因为：[{0}]"),

    CN10052(10052, "从云存储删除文件失败，原因为：[{0}]"),

    CN10053(10053, "短信任务未经过测试，不允许审核"),

    CN10054(10054, "短信任务未经过审核，不允许发送"),

    CN10055(10055, "短信任务已发送完毕，不允许再次发送"),

    CN10056(10056, "未找到记录信息"),

    CN10057(10057, "暂不支持个性化菜单的发布"),

    CN10058(10058, "一级菜单数量已达上限"),

    CN10059(10059, "二级菜单数量已达上限"),

    CN10060(10060, "URL字段不能为空"),

    CN10061(10061, "菜单Key值不能为空"),

    CN10062(10062, "暂时只支持view和click两种响应类型"),

    CN10063(10063, "小程序已存在有效的广告"),

    CN10064(10064, "您已提交过相同类型问题，客服小姐姐正在努力解决中"),

    CN10065(10065, "该订单已经被别的客服受理"),

    CN10066(10066, "帝国[{0}]的指挥官为第一次入库，需要填写该帝国的兵种"),

    CN10067(10067, "游戏中心推荐位已满"),

    CN10068(10068, "游戏中心该推荐位已存在"),

    CN10069(10069, "该福利活动已经存在"),

    CN10070(10070, "答复时间不能晚于完成时间"),

    CN10071(10071, "项目[{0}]无预算"),

    CN10072(10072, "置顶数量已达到最大"),

    CN10073(10073, "该字段已提交过说明"),

    CN10074(10074, "请先配置一二级问题后在发布游戏"),

    CN10075(10075, "该游戏简称已存在"),

    CN10076(10076, "该模板正在使用或者曾经被使用过，不能重复添加"),

    CN10077(10077, "游戏简称不能包含中文"),

    CN10078(10078, "该问题下已配置模板，请勿重复配置"),

    CN10079(10079, "游戏简称中不能含有非法字符"),

    CN10080(10080, "该商品已经存在"),

    CN10081(10081, "付费礼包请填写价格"),

    CN10082(10082, "奖品总概率错误，请重新填写"),

    CN10083(10083, "请选择签到类型"),

    CN10084(10084, "[{0}]栏目下已经存在生效中的公告"),

    CN10085(10085, "截止时间不能小于当前时间"),

    CN10086(10086, "赛事名称已存在"),

    CN10087(10087, "奖品数量不一致，请检查！"),

    CN10088(10088, "advert类型下该端不能为空"),

    CN10089(10089, "系统简称已存在"),

    CN10090(10090, "项目预算不足"),

    CN10091(10091, "合同预算不足"),

    CN10092(10092, "项目预算&合同预算都不足"),

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