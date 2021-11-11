package com.somnus.microservice.commons.base.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.constant
 * @title: GlobalConstant
 * @description: TODO
 * @date 2019/3/18 16:08
 */
public class GlobalConstant {
    /**
     * The constant FILE_MAX_SIZE.
     */
    public static final long FILE_MAX_SIZE = 5 * 1024 * 1024;
    public static final String UNKNOWN = "unknown";

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_CLIENT_IP = "X-Client-IP";
    public static final String X_REAL_IP = "X-Real-IP";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";


    public static final String LOCALHOST_IP = "127.0.0.1";
    public static final String LOCALHOST_IP_16 = "0:0:0:0:0:0:0:1";
    public static final int MAX_IP_LENGTH = 15;

    public static final String DEV_PROFILE = "dev";
    public static final String TEST_PROFILE = "test";
    public static final String PRO_PROFILE = "pro";
    public static final int TWO_INT = 2;
    public static final int M_SIZE = 1024;
    public static final String ROOT_PREFIX = "cloud";

    public static final int EXCEPTION_CAUSE_MAX_LENGTH = 1024;
    public static final int EXCEPTION_MESSAGE_MAX_LENGTH = 1024;

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

    public static final String PREFIX = "prefix";

    public static final String FREQUENT_LOG_PRINT = "frequent.log.print";

    //同人社区数据缓存key
    public static final String tongren_basic_hash_key = "hash_tongren";
    //同人社区缓存key
    public static final String tongren_basic_key = "tongren";

    //内嵌页防止用户伪造纯数字用户id进行接口查询校验密钥
    public static final String INNER_PAGE_SECRET_KEY = "innerPage";

    public interface Number {
        int THOUSAND_INT = 1000;
        int HUNDRED_INT = 100;
        int ONE_INT = 1;
        int TWO_INT = 2;
        int THREE_INT = 3;
        int FOUR_INT = 4;
        int FIVE_INT = 5;
        int SIX_INT = 6;
        int SEVEN_INT = 7;
        int EIGHT_INT = 8;
        int NINE_INT = 9;
        int TEN_INT = 10;
        int EIGHTEEN_INT = 18;
    }


    /**
     * 系统常量
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Sys {
        /**
         * 全局用户名
         */
        public static final String TOKEN_AUTH_DTO = "CURRENT_USER_DTO";
        /**
         * 超级管理员的用户ID
         */
        public static final Long SUPER_MANAGER_USER_ID = 1L;
        /**
         * 超级管理员的用户编号
         */
        public static final String SUPER_MANAGER_LOGIN_NAME = "admin";
        /**
         * 超级管理员角色ID
         */
        public static final Long SUPER_MANAGER_ROLE_ID = 1L;
        /**
         * 超级管理员组织ID
         */
        public static final Long SUPER_MANAGER_GROUP_ID = 1L;
        /**
         * 运营工作台ID
         */
        public static final Long OPER_APPLICATION_ID = 1L;
        /**
         * The constant MENU_ROOT.
         */
        public static final String MENU_ROOT = "root";
        /**
         * The constant DEFAULT_FILE_PATH.
         */
        public static final String DEFAULT_FILE_PATH = "cloud/file/";
        /**
         * redis key default expire = 1MINUTES
         */
        public static final long REDIS_DEFAULT_EXPIRE = 1L;
    }

    /**
     * The class Symbol.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Symbol {
        /**
         * The constant COMMA.
         */
        public static final String COMMA = ",";
        /**
         * The constant FULL_POINT
         */
        public static final String SPOT = ".";
        /**
         * The constant UNDER_LINE.
         */
        public final static String UNDER_LINE = "_";
        /**
         * The constant PER_CENT.
         */
        public final static String PER_CENT = "%";
        /**
         * The constant AT.
         */
        public final static String AT = "@";
        /**
         * The constant PIPE.
         */
        public final static String PIPE = "||";
        public final static String SHORT_LINE = "-";
        public final static String SPACE = " ";
        public static final String SLASH = "/";
        public static final String MH = ":";
    }

    /**
     * The class Oss.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Oss {
        /**
         * The constant DEFAULT_FILE_PATH.
         */
        public static final String DEFAULT_FILE_PATH = "/default/";
    }

    /**
     * 图片压缩高度和宽度
     */
    public static final int IMAGE_WIDTH = 1920;
    /**
     * The constant IMAGE_HEIGHT.
     */
    public static final int IMAGE_HEIGHT = 1280;

    /**
     * The constant Y.
     */
    public static final Integer Y = 1;
    /**
     * The constant N.
     */
    public static final Integer N = 0;

    /**
     * The enum Payment type enum.
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum PaymentTypeEnum {
        /**
         * Online pay payment type enum.
         */
        ONLINE_PAY(1, "在线支付");

        @Getter
        private int code;

        @Getter
        private String value;

        /**
         * Code of payment type enum.
         *
         * @param code the code
         *
         * @return the payment type enum
         */
        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            return null;
        }

    }
}
