package com.somnus.microservice.gateway.config;

import com.somnus.microservice.gateway.web.filter.GrayReactiveLoadBalancerClientFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevin.liu
 * @title: GrayReactiveLoadBalancerConfiguration
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/16 15:36
 */
@Configuration
public class GrayReactiveLoadBalancerConfiguration {

    @Bean
    @ConditionalOnMissingBean({GrayReactiveLoadBalancerClientFilter.class})
    public GrayReactiveLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new GrayReactiveLoadBalancerClientFilter(clientFactory, properties);
    }
}
