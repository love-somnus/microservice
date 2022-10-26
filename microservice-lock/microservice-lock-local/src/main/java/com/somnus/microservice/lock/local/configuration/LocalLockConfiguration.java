package com.somnus.microservice.lock.local.configuration;

import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.configuration.LockAopConfiguration;
import com.somnus.microservice.lock.local.condition.LocalLockCondition;
import com.somnus.microservice.lock.local.impl.LocalLockDelegateImpl;
import com.somnus.microservice.lock.local.impl.LocalLockExecutorImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import java.util.concurrent.locks.Lock;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.local.configuration
 * @title: LocalLockConfiguration
 * @description: TODO
 * @date 2019/6/14 18:03
 */
@AutoConfigureBefore(LockAopConfiguration.class)
@ConditionalOnProperty(prefix = "lock",value = "enabled",havingValue = "true")
public class LocalLockConfiguration {

    @Bean
    @Conditional(LocalLockCondition.class)
    public LockDelegate localLockDelegate(LockExecutor<Lock> executor) {
        return new LocalLockDelegateImpl(executor);
    }

    @Bean
    @Conditional(LocalLockCondition.class)
    public LockExecutor<Lock> localLockExecutor() {
        return new LocalLockExecutorImpl();
    }
}
