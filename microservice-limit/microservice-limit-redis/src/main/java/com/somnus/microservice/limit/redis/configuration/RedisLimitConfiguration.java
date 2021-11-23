package com.somnus.microservice.limit.redis.configuration;

import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.commons.redis.handler.RedisHandlerImpl;
import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.redis.condition.RedisLimitCondition;
import com.somnus.microservice.limit.redis.impl.RedisLimitDelegateImpl;
import com.somnus.microservice.limit.redis.impl.RedisLimitExecutorImpl;
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
public class RedisLimitConfiguration {
    @Bean
    @Conditional(RedisLimitCondition.class)
    public LimitDelegate redisLimitDelegate() {
        return new RedisLimitDelegateImpl();
    }

    @Bean
    @Conditional(RedisLimitCondition.class)
    public LimitExecutor redisLimitExecutor() {
        return new RedisLimitExecutorImpl();
    }

    @Bean
    @Conditional(RedisLimitCondition.class)
    public RedisHandler redisHandler() {
        return new RedisHandlerImpl();
    }
}
