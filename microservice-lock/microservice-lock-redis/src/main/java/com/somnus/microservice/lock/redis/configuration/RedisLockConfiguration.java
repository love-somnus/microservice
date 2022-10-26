package com.somnus.microservice.lock.redis.configuration;

import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.configuration.LockAopConfiguration;
import com.somnus.microservice.lock.redis.condition.RedisLockCondition;
import com.somnus.microservice.lock.redis.impl.RedisLockDelegateImpl;
import com.somnus.microservice.lock.redis.impl.RedisLockExecutorImpl;
import org.redisson.api.RLock;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.redis.configuration
 * @title: RedisLockConfiguration
 * @description: TODO
 * @date 2019/6/14 15:42
 */
@AutoConfigureBefore(LockAopConfiguration.class)
@ConditionalOnProperty(prefix = "lock",value = "enabled",havingValue = "true")
public class RedisLockConfiguration {

    @Bean
    @Conditional(RedisLockCondition.class)
    public LockDelegate redisLockDelegate(LockExecutor<RLock> executor) {
        return new RedisLockDelegateImpl(executor);
    }

    @Bean
    @Conditional(RedisLockCondition.class)
    public LockExecutor<RLock> redisLockExecutor(RedissonHandler redissonHandler) {
        return new RedisLockExecutorImpl(redissonHandler);
    }

}