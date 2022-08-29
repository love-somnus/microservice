package com.somnus.microservice.commons.base.utils;

import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: Objects
 * @projectName webteam
 * @description: TODO
 * @date 2021/2/4 15:46
 */
public abstract class Objects {

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

        D d = mapper.map(s, clazz);

        return d;
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

        List<VO> list = original.stream().map(entity -> mapper.map(entity, clazz)).collect(Collectors.toList());

        return list;
    }

    /**
     * 判断对象是否为空，且对象的所有属性都为空
     * ps: boolean类型会有默认值false 判断结果不会为null 会影响判断结果
     * 序列化的默认值也会影响判断结果
     *
     * @param object
     * @param passList 需要过滤的字段
     * @return
     */
    @SneakyThrows
    public static boolean objCheckIsNull(Object object, List<String> passList) {
        Class<?> clazz = object.getClass(); // 得到类对象
        Field[] fields = clazz.getDeclaredFields(); // 得到所有属性
        boolean flag = true; // 定义返回结果，默认为true
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue;
            // 得到属性类型
            String fieldName = field.getName(); // 得到属性名
            fieldValue = field.get(object); // 得到属性值

            if (ObjectUtils.isEmpty(fieldValue) && !passList.contains(fieldName)) { // 只要有一个属性值为null 就返回false 表示对象存在未填写值
                flag = false;
                break;
            }
        }
        return flag;
    }

}
