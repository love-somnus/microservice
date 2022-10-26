package com.somnus.microservice.autoconfigure.registrar;

import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author Kevin
 * @date 2019/6/14 9:58
 */
public class RegistrarFactoryBean implements ApplicationContextAware, FactoryBean<Object>, InitializingBean, BeanClassLoaderAware {

    private ApplicationContext  applicationContext;

    @Getter
    @Setter
    private Class<?>            interfaze;

    @Getter
    @Setter
    private MethodInterceptor   interceptor;

    private Object              proxy;

    private ClassLoader         classLoader;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public Object getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaze;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addInterface(interfaze);
        proxyFactory.addAdvice(interceptor);
        proxyFactory.setOptimize(false);

        proxy = proxyFactory.getProxy(classLoader);
    }

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}