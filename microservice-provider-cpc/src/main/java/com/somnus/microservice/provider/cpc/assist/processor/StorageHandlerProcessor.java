package com.somnus.microservice.provider.cpc.assist.processor;

import com.google.common.collect.Maps;
import com.somnus.microservice.commons.base.enums.HandlerType;
import com.somnus.microservice.commons.core.support.ClassScaner;
import com.somnus.microservice.provider.cpc.assist.adapter.StorageHandlerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.processor
 * @title: HandlerProcessor
 * @description: TODO
 * @date 2019/12/23 15:52
 */
@Component
public class StorageHandlerProcessor implements BeanFactoryPostProcessor{

    private static final String HANDLER_PACKAGE = "com.somnus.microservice.provider.cpc.assist.handler.storage";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Map<String, Class<?>> handlerMap = Maps.newHashMapWithExpectedSize(2);

        ClassScaner.scan(HANDLER_PACKAGE, HandlerType.class).forEach(clazz -> {
            // 获取注解中的类型值
            String[] types = clazz.getAnnotation(HandlerType.class).values();
            // 将注解中的类型值作为key，对应的类作为value，保存在Map中
            Arrays.stream(types).forEach(type -> handlerMap.put(type, clazz));
        });

        // StorageHandlerAdapter，将其注册到spring容器中
        StorageHandlerAdapter adapter = new StorageHandlerAdapter(handlerMap);
        beanFactory.registerSingleton(StorageHandlerAdapter.class.getName(), adapter);
    }
}