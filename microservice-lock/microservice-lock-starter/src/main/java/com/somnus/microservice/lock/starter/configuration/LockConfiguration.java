package com.somnus.microservice.lock.starter.configuration;

import com.somnus.microservice.lock.configuration.LockAopConfiguration;
import com.somnus.microservice.lock.local.configuration.LocalLockConfiguration;
import com.somnus.microservice.lock.redis.configuration.RedisLockConfiguration;
import com.somnus.microservice.lock.zookeeper.configuration.ZookeeperLockConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.starter.configuration
 * @title: LockConfiguration
 * @description: TODO
 * @date 2019/6/14 17:27
 */
@Configuration
@Import({ LockAopConfiguration.class, RedisLockConfiguration.class, LocalLockConfiguration.class, ZookeeperLockConfiguration.class })
public class LockConfiguration {

}