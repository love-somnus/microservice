package com.somnus.microservice.commons.base.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: JacksonUtil
 * @description: Jackson Json 工具类
 * @date 2019/3/19 14:48
 */
@UtilityClass
public class JacksonUtil {

    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    private static final ObjectMapper defaultMapper;

    private static final ObjectMapper formatedMapper;

    static {
        // 默认的ObjectMapper
        defaultMapper = new ObjectMapper();
        defaultMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        defaultMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        defaultMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        defaultMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        defaultMapper.enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        formatedMapper = new ObjectMapper();
        formatedMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        formatedMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        formatedMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        formatedMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        formatedMapper.enable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        // java8日期日期处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        formatedMapper.registerModule(javaTimeModule);

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
    @SneakyThrows(IOException.class)
    public static <T> T parseJson(String jsonValue, JavaType valueType){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return defaultMapper.readValue(jsonValue, valueType);
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
    @SneakyThrows(IOException.class)
    public static <T> T parseJson(String jsonValue, TypeReference<T> valueTypeRef){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return defaultMapper.readValue(jsonValue, valueTypeRef);
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
    @SneakyThrows(IOException.class)
    public static <T> T parseJsonWithFormat(String jsonValue, JavaType valueType) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "this argument is required; it must not be null");
        return formatedMapper.readValue(jsonValue, valueType);
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
    @SneakyThrows(IOException.class)
    public static <T> T parseJsonWithFormat(String jsonValue, TypeReference<T> valueTypeRef){
        Preconditions.checkArgument(StringUtils.isNotEmpty(jsonValue), "jsonValue is not null");
        return formatedMapper.readValue(jsonValue, valueTypeRef);
    }

}