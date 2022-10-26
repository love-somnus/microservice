package com.somnus.microservice.limit.redis.configuration;

import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.commons.redis.handler.RedisHandlerImpl;
import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.configuration.LimitAopConfiguration;
import com.somnus.microservice.limit.redis.condition.RedisLimitCondition;
import com.somnus.microservice.limit.redis.impl.RedisLimitDelegateImpl;
import com.somnus.microservice.limit.redis.impl.RedisLimitExecutorImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.configuration
 * @title: RedisLimitConfiguration
 * @description: TODO
 * @date 2019/7/10 17:06
 */
@AutoConfigureBefore(LimitAopConfiguration.class)
@ConditionalOnProperty(prefix = "limit",value = "enabled",havingValue = "true")
public class RedisLimitConfiguration {
    @Bean
    @Conditional(RedisLimitCondition.class)
    public LimitDelegate redisLimitDelegate(LimitExecutor executor) {
        return new RedisLimitDelegateImpl(executor);
    }

    @Bean
    @Conditional(RedisLimitCondition.class)
    public LimitExecutor redisLimitExecutor(RedisHandler redisHandler) {
        return new RedisLimitExecutorImpl(redisHandler);
    }

    @Bean
    @Conditional(RedisLimitCondition.class)
    @ConditionalOnMissingBean(RedisHandler.class)
    public RedisHandler redisHandler(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisHandlerImpl(redisTemplate, stringRedisTemplate);
    }
}
