package com.somnus.microservice.limit.local.configuration;

import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.configuration.LimitAopConfiguration;
import com.somnus.microservice.limit.local.condition.LocalLimitCondition;
import com.somnus.microservice.limit.local.impl.GuavaLocalLimitExecutorImpl;
import com.somnus.microservice.limit.local.impl.LocalLimitDelegateImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.local.configuration
 * @title: LocalLimitConfiguration
 * @description: TODO
 * @date 2019/7/10 16:50
 */
@AutoConfigureBefore(LimitAopConfiguration.class)
@ConditionalOnProperty(prefix = "limit",value = "enabled",havingValue = "true")
public class LocalLimitConfiguration {

    @Bean
    @Conditional(LocalLimitCondition.class)
    public LimitDelegate localLimitDelegate(LimitExecutor executor) {
        return new LocalLimitDelegateImpl(executor);
    }

    @Bean
    @Conditional(LocalLimitCondition.class)
    public LimitExecutor localLimitExecutor() {
        return new GuavaLocalLimitExecutorImpl();
    }
}
