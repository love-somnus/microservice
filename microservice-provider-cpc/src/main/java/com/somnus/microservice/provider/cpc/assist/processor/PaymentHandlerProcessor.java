package com.somnus.microservice.provider.cpc.assist.processor;

import com.somnus.microservice.provider.cpc.assist.adapter.PaymentHandlerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author kevin.liu
 * @title: PaymentHandlerProcessor
 * @projectName webteam
 * @description: TODO
 * @date 2021/1/25 17:27
 */
@Component
public class PaymentHandlerProcessor extends HandlerPostProcessor<PaymentHandlerAdapter> implements BeanFactoryPostProcessor {

    private static final String HANDLER_PACKAGE = "com.somnus.microservice.provider.cpc.assist.handler.payment";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        // PaymentHandlerAdapter，将其注册到spring容器中
        PaymentHandlerAdapter adapter = adapter(HANDLER_PACKAGE, PaymentHandlerAdapter::new);

        beanFactory.registerSingleton(PaymentHandlerAdapter.class.getName(), adapter);
    }
}
