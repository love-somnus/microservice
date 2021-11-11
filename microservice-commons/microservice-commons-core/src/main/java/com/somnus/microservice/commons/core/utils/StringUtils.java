package com.somnus.microservice.commons.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.utils
 * @title: StringUtils
 * @description: TODO
 * @date 2020/10/21 15:11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class StringUtils {

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param  character 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String character) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < character.length(); i++) {
            // 获取一个字符
            String temp = character.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为2
                length += 2;
            } else {
                // 其他字符长度为1
                length += 1;
            }
        }
        return  length;
    }

    /**
     * 分割条数
     * @param character
     * @param maxlen
     * @return
     */
    public static int times(String character, int maxlen){
        if(length(character) % maxlen == 0) {
            return length(character) / maxlen;
        }
        return length(character) / maxlen + 1;
    }

    public static Integer countable(String words){

        String line = words.replaceAll("['\";:,.?¿\\-!¡]+", "");

        Pattern p = Pattern.compile("\\S+");

        Matcher ms = p.matcher(line);

        AtomicInteger atomicInteger = new AtomicInteger();

        while(ms.find()){
            atomicInteger.addAndGet(1);
        }

        return atomicInteger.get();
    }

}
