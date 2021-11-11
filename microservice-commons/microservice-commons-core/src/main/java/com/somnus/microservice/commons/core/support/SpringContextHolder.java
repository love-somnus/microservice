package com.somnus.microservice.commons.core.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.support
 * @title: SpringContextHolder
 * @description: TODO
 * @date 2019/4/16 17:34
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * Sets application context.
     *
     * @param applicationContext the application context
     *
     * @throws BeansException the beans exception
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextHolder.applicationContext == null) {
            SpringContextHolder.applicationContext = applicationContext;
        }
    }

    /**
     * Gets application context.
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    /**
     * Gets bean.
     *
     * @param beanName the bean name
     *
     * @return the bean
     */
    public static Object getBean(String beanName) {
        assertApplicationContext();
        return applicationContext.getBean(beanName);
    }

    /**
     * Gets bean.
     *
     * @param <T>          the type parameter
     *
     * @return the bean
     */
    public static <T> T getBean(Class<T> clazz) {
        assertApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * Gets bean.
     *
     * @param <T>          the type parameter
     *
     * @return the bean
     */
    public static <T> T getBean(String name, Class<T> clazz){
        assertApplicationContext();
        return applicationContext.getBean(name, clazz);
    }

    public static DefaultListableBeanFactory getDefaultListableBeanFactory() {
        assertApplicationContext();
        return (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
    }

    private static void assertApplicationContext() {
        if (SpringContextHolder.applicationContext == null) {
            throw new IllegalArgumentException("applicationContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }

}