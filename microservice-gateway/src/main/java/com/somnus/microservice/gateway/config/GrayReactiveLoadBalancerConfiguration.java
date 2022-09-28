package com.somnus.microservice.gateway.config;

import com.somnus.microservice.gateway.web.filter.IpGrayReactiveLoadBalancerClientFilter;
import com.somnus.microservice.gateway.web.filter.VerGrayReactiveLoadBalancerClientFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    @ConditionalOnProperty(prefix = "secure.gray-version",value = "enable",havingValue = "true")
    public VerGrayReactiveLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new VerGrayReactiveLoadBalancerClientFilter(clientFactory, properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "secure.gray-ip",value = "enable",havingValue = "true")
    public IpGrayReactiveLoadBalancerClientFilter ipGrayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        return new IpGrayReactiveLoadBalancerClientFilter(clientFactory, properties);
    }
}
