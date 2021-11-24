package com.somnus.microservice.cache.starter.configuration;

import com.somnus.microservice.cache.configuration.CacheAopConfiguration;
import com.somnus.microservice.cache.redis.configuration.RedisCacheConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache.starter.configuration
 * @title: CacheConfiguration
 * @description: TODO
 * @date 2019/7/5 16:59
 */
@Configuration
@Import({ CacheAopConfiguration.class, RedisCacheConfiguration.class })
public class CacheConfiguration {

}