package com.somnus.microservice.commons.utils;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyUtil {

	/**
	 * The constant RESET_PWD_TOKEN_KEY.
	 */
	private static final String RESET_PWD_TOKEN_KEY = "wt:restPwd";
	private static final String ES_MEMBER_MODIFY_COUNT = "wt:es:member:modify:count";
	private static final String ES_ANCHOR_MODIFY_COUNT = "wt:es:anchor:modify:count";
	private static final String ACTIVE_USER = "wt:activeUser";
	private static final String SEND_SMS_COUNT = "wt:sms:count";
	private static final String SEND_SMS_CODE = "wt:sms:code:{0}:{1}";
	private static final String SEND_EMAIL_CODE = "wt:email:code:{0}:{1}";
	private static final String ACCESS_TOKEN = "wt:token:accessToken";
	private static final String UPLOAD_FILE_SIZE = "wt:file:upload_file_size";
	private static final String VOTE_IP = "wt:vote:ip";
	private static final String ORDER_STATISTICS = "wt:order:statistics";
	private static final String ORDER_RESTRICT_IP = "wt:restrict:ip";
	private static final int REF_NO_MAX_LENGTH = 100;

	public static final String USER_INFO = "wt:user_info";
	public static final String MINI_USER_INFO = "wt:user_info:mini";//小程序用户信息
	public static final String BASE_KEY = "brand:ticket:";

	/**
	 * Gets order statistics key.
	 *
	 * @param alias
	 *
	 * @return
	 */
	public static String getOrderStatisticsKey(String alias) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(alias), "参数不能为空");
		return ORDER_STATISTICS + ":" + alias;
	}

	/**
	 * Gets reset pwd token key.
	 *
	 * @param mobile
	 *
	 * @return
	 */
	public static String getResetPwdTokenKey(String mobile) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(mobile), "参数不能为空");
		return RESET_PWD_TOKEN_KEY + ":" + mobile;
	}

	/**
	 *
	 * @param ipAddr
	 *
	 * @return
	 */
	public static String getVoteIpKey(String ipAddr) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "参数不能为空");
		return VOTE_IP + ":" + ipAddr;
	}

	/**
	 *
	 * @param ipAddr
	 *
	 * @return
	 */
	public static String getOrderRestrictIpKey(String ipAddr) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "参数不能为空");
		return ORDER_RESTRICT_IP + ":" + ipAddr;
	}

	/**
	 * Gets reset pwd token key.
	 *
	 * @param mobile t
	 *
	 * @return
	 */
	public static String getEsMemberModifyCountKey(String mobile) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(mobile), "参数不能为空");
		return ES_MEMBER_MODIFY_COUNT + ":" + mobile;

	}

	/**
	 * Gets reset pwd token key.
	 *
	 * @param modifyCount the rest pwd key
	 *
	 * @return the reset pwd token key
	 */
	public static String getEsAnchorModifyCountKey(String modifyCount) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(modifyCount), "参数不能为空");
		return ES_ANCHOR_MODIFY_COUNT + ":" + modifyCount;

	}

	public static String getSendEmailCodeKey(String game, String email) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(game), "游戏不能为空");
		Preconditions.checkArgument(StringUtils.isNotEmpty(email), "email不能为空");
		return MessageFormat.format(SEND_EMAIL_CODE, game, email);
	}

	/**
	 * Gets active user key.
	 *
	 * @param activeToken the active token
	 *
	 * @return the active user key
	 */
	public static String getActiveUserKey(String activeToken) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(activeToken), "参数不能为空");
		return ACTIVE_USER + ":" + activeToken;

	}

	/**
	 * Gets send sms count key.
	 *
	 * @param ipAddr the ip addr
	 * @param type   mobile;ip;total
	 *
	 * @return the send sms count key
	 */
	public static String getSendSmsCountKey(String ipAddr, String type) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "请不要篡改IP地址");
		return SEND_SMS_COUNT + ":" + type + ":" + ipAddr;

	}


	/**
	 * Gets send sms rate key.
	 *
	 * @param ipAddr the ip addr
	 *
	 * @return the send sms rate key
	 */
	public static String getSendSmsRateKey(String ipAddr) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(ipAddr), "请不要篡改IP地址");
		return SEND_SMS_COUNT + ":" + ipAddr;

	}

	/**
	 * Gets send sms rate key.
	 *
	 * @param mobile the mobile
	 *
	 * @return the send sms rate key
	 */
	public static String getSendSmsCodeKey(String game, String mobile) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(game), "游戏不能为空");
		Preconditions.checkArgument(StringUtils.isNotEmpty(mobile), "手机号不能为空");
		return MessageFormat.format(SEND_SMS_CODE, game, mobile);
	}

	public static String getAccessTokenKey(String token) {
		Preconditions.checkArgument(StringUtils.isNotEmpty(token), "非法请求token参数不存在");
		return ACCESS_TOKEN + ":" + token;
	}

	public static String createMqKey(String topic, String tag, String refNo, String body) {
		String temp = refNo;
		Preconditions.checkArgument(StringUtils.isNotEmpty(topic), "topic is null");
		Preconditions.checkArgument(StringUtils.isNotEmpty(tag), "tag is null");
		Preconditions.checkArgument(StringUtils.isNotEmpty(refNo), "refNo is null");
		Preconditions.checkArgument(StringUtils.isNotEmpty(body), "body is null");

		if (refNo.length() > REF_NO_MAX_LENGTH) {
			temp = StringUtils.substring(refNo, 0, REF_NO_MAX_LENGTH) + "...";
		}
		return topic + "_" + tag + "_" + temp + "-" + body.hashCode();
	}

	public static String getFileSizeKey() {
		return UPLOAD_FILE_SIZE;
	}
}
