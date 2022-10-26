package com.somnus.microservice.limit.starter.configuration;

import com.somnus.microservice.limit.configuration.LimitAopConfiguration;
import com.somnus.microservice.limit.local.configuration.LocalLimitConfiguration;
import com.somnus.microservice.limit.redis.configuration.RedisLimitConfiguration;
import com.somnus.microservice.limit.redisson.configuration.RedissonLimitConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.starter.configuration
 * @title: LimitConfiguration
 * @description: TODO
 * @date 2019/7/10 17:14
 */
@Import({ LimitAopConfiguration.class, RedisLimitConfiguration.class, RedissonLimitConfiguration.class, LocalLimitConfiguration.class })
public class LimitConfiguration {

}