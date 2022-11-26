package com.somnus.microservice.provider.cpc.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * @author kevin.liu
 * @date 2022/11/16 11:50
 */
public class RefreshConfigurer implements InitializingBean, ApplicationListener<EnvironmentChangeEvent> {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void onApplicationEvent(@NonNull EnvironmentChangeEvent event) {

    }
}
