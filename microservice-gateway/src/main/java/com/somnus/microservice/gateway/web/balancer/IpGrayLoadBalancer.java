package com.somnus.microservice.gateway.web.balancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @date 2022/9/28 12:25
 */
@Slf4j
public class IpGrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    /**
     * ip 远程调用者ip地址
     */
    private final String ip;

    /**
     * loadbalancer 提供的访问当前服务的名称
     */
    private final String serviceId;

    private final AtomicInteger position;

    /**
     * loadbalancer 提供的访问的服务列表
     */
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public IpGrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, String ip) {
        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000), ip);
    }

    public IpGrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition, String ip) {
        this.ip = ip;
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);

        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(serviceInstances, request));
    }

    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> instances, Request<?> request) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + this.serviceId);
            }
            return new EmptyResponse();
        }

        List<ServiceInstance> serviceInstances = instances.stream()
                .filter(instance -> {
                    String ips = instance.getMetadata().get("ip");
                    AntPathMatcher antPathMatcher = new AntPathMatcher();
                    return Arrays.stream(ips.split("[|]")).anyMatch(regex -> antPathMatcher.match(regex, ip));
                })
                .collect(Collectors.toList());

        if (serviceInstances.size() > 0) {
            return processInstanceResponse(serviceInstances);
        } else {
            //排除被指定了ip的灰度服务
            List<ServiceInstance> availableInstances = instances.stream()
                    .filter(instance -> {
                        String ips = instance.getMetadata().get("ip");
                        AntPathMatcher antPathMatcher = new AntPathMatcher();
                        return Arrays.stream(ips.split("[|]")).anyMatch(regex -> antPathMatcher.match(regex, "release"));
                    })
                    .collect(Collectors.toList());
            return processInstanceResponse(availableInstances);
        }
    }

    /**
     * 负载均衡器
     * 参考 org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer#getInstanceResponse
     */
    private Response<ServiceInstance> processInstanceResponse(List<ServiceInstance> instances) {
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }
}
