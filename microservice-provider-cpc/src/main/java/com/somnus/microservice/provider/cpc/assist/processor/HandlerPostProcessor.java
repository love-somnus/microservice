package com.somnus.microservice.provider.cpc.assist.processor;

import com.google.common.collect.Maps;
import com.somnus.microservice.commons.base.enums.HandlerType;
import com.somnus.microservice.commons.core.support.ClassScaner;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * @author kevin.liu
 * @title: HandlerPostProcessor
 * @projectName microservice
 * @description: TODO
 * @date 2022/8/11 20:47
 */
public abstract class HandlerPostProcessor<T>{

    public T adapter(String handlerPackage, Function<Map<String, Class<?>>, T> function){

        Map<String, Class<?>> handlerMap = Maps.newHashMapWithExpectedSize(2);

        ClassScaner.scan(handlerPackage, HandlerType.class).forEach(clazz -> {

            // 获取注解中的类型值
            String[] types = clazz.getAnnotation(HandlerType.class).values();

            // 将注解中的类型值作为key，对应的类作为value，保存在Map中
            Arrays.stream(types).forEach(type -> handlerMap.put(type, clazz));
        });

        return function.apply(handlerMap);
    }
}