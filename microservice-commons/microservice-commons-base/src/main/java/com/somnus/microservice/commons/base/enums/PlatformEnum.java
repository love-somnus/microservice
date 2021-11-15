package com.somnus.microservice.commons.base.enums;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: PlatformEnum
 * @description: TODO
 * @date 2019/5/16 13:38
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlatformEnum {

    WINDOWS("Windows", new String[]{"Windows","Windows 10","Windows 8.1","Windows 8","Windows 7","Windows Vista","Windows 2000","Windows XP","Windows 98"}),

    WINDOWSPHONE("WindowsPhone", new String[]{"Windows 10 Mobile","Windows Phone 8.1","Windows Phone 8","Windows Phone 7","Windows Mobile"}),

    MAC("Mac", new String[]{"Mac OS X", "Mac OS"}),

    ANDROID("Android", new String[]{"Android","Android 8.x","Android 8.x Tablet","Android 7.x","Android 7.x Tablet",
            "Android 6.x","Android 6.x Tablet","Android 5.x","Android 5.x Tablet","Android 4.x","Android 4.x Tablet"
            ,"Android 3.x Tablet","Android 2.x","Android 2.x Tablet","Android 1.x","Android Mobile","Android Tablet"}),

    IPHONE("iPhone", new String[]{"iOS","iOS 11 (iPhone)","iOS 10 (iPhone)","iOS 9 (iPhone)","iOS 8.4 (iPhone)","iOS 8.3 (iPhone)"
            ,"iOS 8.2 (iPhone)","iOS 8.1 (iPhone)","iOS 8 (iPhone)","iOS 7 (iPhone)","iOS 6 (iPhone)","iOS 5 (iPhone)"
            ,"iOS 4 (iPhone)","Mac OS X (iPhone)"}),

    IPAD("iPad", new String[]{"Mac OS X (iPad)","iOS 11 (iPad)","iOS 10 (iPad)","iOS 9 (iPad)","iOS 8.4 (iPad)","iOS 8.3 (iPad)"
            ,"iOS 8.2 (iPad)","iOS 8.1 (iPad)","iOS 8 (iPad)","iOS 7 (iPad)","iOS 6 (iPad)"}),

    UNKNOWN("Unknown", new String[]{"Unknown"});

    private String platform;

    private String os[];

    /**
     * Gets enum.
     *
     * @param userAgent the name
     *
     * @return the enum
     */
    public static PlatformEnum platformOf(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        /*Browser browser = agent.getBrowser();*/
        OperatingSystem os = agent.getOperatingSystem();
        for (PlatformEnum platform : PlatformEnum.values()) {
            if(Arrays.stream(platform.getOs()).anyMatch(item -> os.getName().equals(item))){
                return platform;
            }
        }
        return null;
    }

    /**
     * Gets enum.
     *
     * @param platform the platform
     *
     * @return the enum
     */
    public static PlatformEnum platOf(String platform) {
        for (PlatformEnum plat : PlatformEnum.values()) {
            if (plat.name().equalsIgnoreCase(platform)) {
                return plat;
            }
        }
        return null;
    }

}
