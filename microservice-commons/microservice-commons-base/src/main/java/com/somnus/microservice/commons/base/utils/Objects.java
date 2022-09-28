package com.somnus.microservice.commons.base.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: Objects
 * @description: TODO
 * @date 2021/2/4 15:46
 */
@UtilityClass
public class Objects extends org.springframework.util.ObjectUtils{

    /**
     * 数组不为空
     * @param array
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object[] array){
        return !isEmpty(array);
    }

    /**
     * 对象不为空
     * @param obj
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj){
        return !isEmpty(obj);
    }

    /**
     * Object 转换为map
     * @param src
     * @return Map
     */
    public static Map<?, ?> beanToMap(Object src){
        return org.springframework.cglib.beans.BeanMap.create(src);
    }

    /**
     * 对象转换
     * @param s
     * @param clazz
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D convert(S s, Class<D> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(s, clazz);
    }

    /**
     * 对象转换
     * @param s
     * @param clazz
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D convert(S s, Class<D> clazz, BiConsumer<S, D> consumer){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        D d = mapper.map(s, clazz);

        consumer.accept(s, d);

        return d;
    }

    /**
     * 对象转换DOMAIN -> VO
     * @param original
     * @param <DOMAIN>
     * @param <VO>
     * @return
     */
    public static <DOMAIN, VO> List<VO> convert(List<DOMAIN> original, Class<VO> clazz){

        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return original.parallelStream().map(entity -> mapper.map(entity, clazz)).collect(Collectors.toList());
    }

}
