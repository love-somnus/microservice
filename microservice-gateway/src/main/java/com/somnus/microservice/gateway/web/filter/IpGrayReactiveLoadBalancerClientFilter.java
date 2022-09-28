package com.somnus.microservice.gateway.web.filter;

import com.somnus.microservice.commons.base.utils.ReactiveRequestUtil;
import com.somnus.microservice.gateway.web.balancer.IpGrayLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * @author kevin.liu
 * @date 2022/9/27 14:31
 * @see org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration
 * @see org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter
 * @see org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer
 */
@Slf4j
public class IpGrayReactiveLoadBalancerClientFilter implements GlobalFilter, Ordered {

    private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

    private final LoadBalancerClientFactory clientFactory;

    private final GatewayLoadBalancerProperties properties;

    public IpGrayReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        this.clientFactory = clientFactory;
        this.properties = properties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);

        /* 通过匹配配置中的url前缀是否含有gray-lb执行以下过滤器，如果前缀中不含有gray-lb则走默认的负载均衡 */
        if (url != null && ("gray-lb".equals(url.getScheme()) || "gray-lb".equals(schemePrefix))) {
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);

            if (log.isTraceEnabled()) {
                log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
            }

            return this.choose(exchange).doOnNext((response) -> {

                if (!response.hasServer()) {
                    throw NotFoundException.create(properties.isUse404(), "Unable to find instance for " + url.getHost());
                }

                URI uri = exchange.getRequest().getURI();

                String overrideScheme = Optional.ofNullable(schemePrefix).map(v -> url.getScheme()).orElse(null);

                DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(response.getServer(), overrideScheme);

                URI requestUrl = LoadBalancerUriTools.reconstructURI(serviceInstance, uri);

                if (log.isTraceEnabled()) {
                    log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                }

                exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
            }).then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }

    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String ip = ReactiveRequestUtil.getRemoteAddress(exchange.getRequest());
        IpGrayLoadBalancer loadBalancer = new IpGrayLoadBalancer(clientFactory.getLazyProvider(Objects.requireNonNull(uri).getHost(), ServiceInstanceListSupplier.class), uri.getHost(), ip);
        return Optional.of(loadBalancer)
                .map(v -> loadBalancer.choose(new DefaultRequest<>(exchange.getRequest().getHeaders())))
                .orElseThrow(() -> new NotFoundException("No loadbalancer available for " + uri.getHost()));
    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

}
