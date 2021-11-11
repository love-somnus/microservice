package com.somnus.microservice.commons.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.enums
 * @title: ResourcesEnum
 * @description: TODO
 * @date 2019/3/28 11:45
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResourcesEnum {

    ARTICLE("article","文章"),

    IMAGE("image","图片"),

    VIDEO("video","视频"),

    LINK("link","链接"),

    MEDIA("media","媒体");

    @Getter
    private String type;

    @Getter
    private String desc;

    /**
     * Gets enum.
     *
     * @param type the type
     *
     * @return the enum
     */
    public static ResourcesEnum typeOf(String type) {
        for (ResourcesEnum ele : ResourcesEnum.values()) {
            if (ele.getType().equalsIgnoreCase(type)) {
                return ele;
            }
        }
        return null;
    }

    /**
     * Gets enum.
     *
     * @param name the name
     *
     * @return the enum
     */
    public static ResourcesEnum nameOf(String name) {
        for (ResourcesEnum value : ResourcesEnum.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
