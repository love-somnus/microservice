package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: ResourcesEnum
 * @description: TODO
 * @date 2019/3/28 11:45
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConuntryEnum {

    CHINA("86","中国大陆"),

    TAIWAN("886","中国台湾"),

    HONGKONG("852","中国香港"),

    MACAO("853","中国澳门"),

    Malaysia("60","马来西亚"),

    Thailand("66","泰国"),

    Vietnam("84","越南"),

    Indonesia("62","印度尼西亚"),

    JAPAN("81","日本"),

    Korea("82","韩国"),

    Myanmar("95","缅甸"),

    Cambodia("855","柬埔寨"),

    LAOS("856","老挝"),

    SINGAPORE("65","新加坡");

    @Getter
    private String code;

    @Getter
    private String district;

    private static final String[] countryCodes = new String[]{"86", "886", "852", "853", "65",  "66", "60", "62", "856", "855", "81", "82", "84", "95"};

    /**
     * Gets enum.
     *
     * @param code the code
     *
     * @return the enum
     */
    public static ConuntryEnum codeOf(String code) {
        for (ConuntryEnum ele : ConuntryEnum.values()) {
            if (ele.getCode().equalsIgnoreCase(code)) {
                return ele;
            }
        }
        return null;
    }

    public static ConuntryEnum mobileOf(String mobile) {
        Long number = Long.valueOf(mobile);
        String countryCode = Arrays.stream(countryCodes).filter(item -> StringUtils.startsWith(number.toString(), item)).findFirst().orElse("86");
        for (ConuntryEnum ele : ConuntryEnum.values()) {
            if (ele.getCode().equalsIgnoreCase(countryCode)) {
                return ele;
            }
        }
        return null;
    }

}
