package com.somnus.microservice.lock.zookeeper.configuration;

import com.somnus.microservice.commons.base.constant.GlobalConstant;
import com.somnus.microservice.commons.zookeeper.handler.CuratorHandler;
import com.somnus.microservice.commons.zookeeper.handler.CuratorHandlerImpl;
import com.somnus.microservice.lock.LockDelegate;
import com.somnus.microservice.lock.LockExecutor;
import com.somnus.microservice.lock.zookeeper.condition.ZookeeperLockCondition;
import com.somnus.microservice.lock.zookeeper.impl.ZookeeperLockDelegateImpl;
import com.somnus.microservice.lock.zookeeper.impl.ZookeeperLockExecutorImpl;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.zookeeper.configuration
 * @title: ZookeeperLockConfiguration
 * @description: TODO
 * @date 2019/7/30 14:43
 */
@Configuration
public class ZookeeperLockConfiguration {

    @Value("${" + GlobalConstant.PREFIX + "}")
    private String prefix;

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockDelegate zookeeperLockDelegate(LockExecutor<InterProcessMutex> lockExecutor) {
        return new ZookeeperLockDelegateImpl(lockExecutor);
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockExecutor<InterProcessMutex> zookeeperLockExecutor(CuratorHandler curatorHandler) {
        return new ZookeeperLockExecutorImpl(curatorHandler);
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public CuratorHandler curatorHandler() {
        return new CuratorHandlerImpl(prefix);
    }
}