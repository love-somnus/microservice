package com.somnus.microservice.limit.redisson.configuration;

import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.redisson.condition.RedissonLimitCondition;
import com.somnus.microservice.limit.redisson.impl.RedissonLimitDelegateImpl;
import com.somnus.microservice.limit.redisson.impl.RedissonLimitExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.configuration
 * @title: RedisLimitConfiguration
 * @description: TODO
 * @date 2019/7/10 17:06
 */
@Configuration
public class RedissonLimitConfiguration {

    @Bean
    @Conditional(RedissonLimitCondition.class)
    public LimitDelegate redisLimitDelegate(LimitExecutor executor) {
        return new RedissonLimitDelegateImpl(executor);
    }

    @Bean
    @Conditional(RedissonLimitCondition.class)
    public LimitExecutor redisLimitExecutor(RedissonHandler redissonHandler) {
        return new RedissonLimitExecutorImpl(redissonHandler);
    }
}
