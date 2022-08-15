package com.somnus.microservice.provider.cpc.assist.processor;

import com.somnus.microservice.provider.cpc.assist.adapter.StorageHandlerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.processor
 * @title: StorageHandlerProcessor
 * @description: TODO
 * @date 2019/12/23 15:52
 */
@Component
public class StorageHandlerProcessor extends HandlerPostProcessor<StorageHandlerAdapter> implements BeanFactoryPostProcessor{

    private static final String HANDLER_PACKAGE = "com.somnus.microservice.provider.cpc.assist.handler.storage";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        // StorageHandlerAdapter，将其注册到spring容器中
        StorageHandlerAdapter adapter = adapter(HANDLER_PACKAGE, StorageHandlerAdapter::new);

        beanFactory.registerSingleton(StorageHandlerAdapter.class.getName(), adapter);
    }
}