package com.somnus.microservice.commons.base.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: JacksonUtil
 * @description: Jackson Json 工具类
 * @date 2019/3/19 14:48
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {

    private static ObjectMapper defaultMapper;
    private static ObjectMapper formatedMapper;

    static {
        // 默认的ObjectMapper
        defaultMapper = new ObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        defaultMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

        formatedMapper = new ObjectMapper();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        formatedMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        // 所有日期格式都统一为固定格式
        formatedMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将对象转化为json数据
     *
     * @param obj the obj
     *
     * @return string string
     *
     * @throws IOException the io exception
     */
    @SneakyThrows(IOException.class)
    public static String toJson(Object obj) {
        Preconditions.checkArgument(obj != null, "this argument is required; it must not be null");
        return defaultMapper.writeValueAsString(obj);
    }

    /**
     * json数据转化为对象(Class)
     * User u = JacksonUtil.parseJson(jsonValue, User.class);
     * User[] arr = JacksonUtil.parseJson(jsonValue, User[].class);
     *
     * @param <T>       the type parameter
     * @param jsonValue the json value
     * @param valueType the value type
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SneakyThrows(IOException.class)
    public static <T> T parseJson(String jsonValue, Class<T> valueType) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return defaultMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(JavaType)
     *
     * @param <T>       the type parameter
     * @param jsonValue the json value
     * @param valueType the value type
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static <T> T parseJson(String jsonValue, JavaType valueType){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) defaultMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(TypeReference)
     *
     * @param <T>          the type parameter
     * @param jsonValue    the json value
     * @param valueTypeRef the value type ref
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static <T> T parseJson(String jsonValue, TypeReference<T> valueTypeRef){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) defaultMapper.readValue(jsonValue, valueTypeRef);
    }

    /**
     * 将对象转化为json数据(时间转换格式： "yyyy-MM-dd HH:mm:ss")
     *
     * @param obj the obj
     *
     * @return string string
     *
     * @throws IOException the io exception
     */
    @SneakyThrows(IOException.class)
    public static String toJsonWithFormat(Object obj){
        Preconditions.checkArgument(obj != null, "this argument is required; it must not be null");
        return formatedMapper.writeValueAsString(obj);
    }

    /**
     * json数据转化为对象(时间转换格式： "yyyy-MM-dd HH:mm:ss")
     * User u = JacksonUtil.parseJsonWithFormat(jsonValue, User.class);
     * User[] arr = JacksonUtil.parseJsonWithFormat(jsonValue, User[].class);
     *
     * @param <T>       the type parameter
     * @param jsonValue the json value
     * @param valueType the value type
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SneakyThrows(IOException.class)
    public static <T> T parseJsonWithFormat(String jsonValue, Class<T> valueType) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return formatedMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(JavaType)
     *
     * @param <T>       the type parameter
     * @param jsonValue the json value
     * @param valueType the value type
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static <T> T parseJsonWithFormat(String jsonValue, JavaType valueType) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return (T) formatedMapper.readValue(jsonValue, valueType);
    }

    /**
     * json数据转化为对象(TypeReference)
     *
     * @param <T>          the type parameter
     * @param jsonValue    the json value
     * @param valueTypeRef the value type ref
     *
     * @return t t
     *
     * @throws IOException the io exception
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static <T> T parseJsonWithFormat(String jsonValue, TypeReference<T> valueTypeRef){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "jsonValue is not null");
        return (T) formatedMapper.readValue(jsonValue, valueTypeRef);
    }

}