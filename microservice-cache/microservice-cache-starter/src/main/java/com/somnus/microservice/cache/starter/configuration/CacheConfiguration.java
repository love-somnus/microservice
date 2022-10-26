package com.somnus.microservice.cache.starter.configuration;

import com.somnus.microservice.cache.configuration.CacheAopConfiguration;
import com.somnus.microservice.cache.redis.configuration.RedisCacheConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @date 2019/7/5 16:59
 */
@Import({ CacheAopConfiguration.class, RedisCacheConfiguration.class })
public class CacheConfiguration {

}