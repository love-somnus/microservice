package com.somnus.microservice.elasticjob.autoconfigure;

import com.somnus.microservice.elasticjob.dynamic.service.JobService;
import com.somnus.microservice.elasticjob.parser.JobConfParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
/**
 * @author somnus
 * @packageName com.somnus.microservice.elasticjob.autoconfigure
 * @title: JobParserAutoConfiguration
 * @description: 任务自动配置
 * @date 2021/10/25 17:57
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ZookeeperProperties.class)
public class JobParserAutoConfiguration {

    private final ZookeeperProperties zookeeperProperties;

    /**
     * 初始化Zookeeper注册中心
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter() {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());
        zkConfig.setBaseSleepTimeMilliseconds(zookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setConnectionTimeoutMilliseconds(zookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setDigest(zookeeperProperties.getDigest());
        zkConfig.setMaxRetries(zookeeperProperties.getMaxRetries());
        zkConfig.setMaxSleepTimeMilliseconds(zookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(zookeeperProperties.getSessionTimeoutMilliseconds());
        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    public JobConfParser jobConfParser(JobService jobService, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        return new JobConfParser(jobService, zookeeperRegistryCenter);
    }

}