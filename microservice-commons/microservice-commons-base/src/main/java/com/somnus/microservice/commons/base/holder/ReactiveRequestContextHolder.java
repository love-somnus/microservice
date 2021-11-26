package com.somnus.microservice.commons.base.holder;

import org.springframework.core.NamedThreadLocal;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author kevin.liu
 * @title: ReactiveRequestContextHolder
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/26 15:08
 */
public class ReactiveRequestContextHolder {

    public static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    private static final ThreadLocal<ServerHttpRequest> requests = new NamedThreadLocal<>("Thread ServerHttpRequest");

    /**
     * Store {@link ServerHttpRequest} to {@link ThreadLocal} in the current thread
     *
     * @param request {@link ServerHttpRequest}
     */
    public static void put(ServerHttpRequest request){
        if(Objects.nonNull(get())) {
            requests.remove();
        }
        if(request != null){
            requests.set(request);
        }
    }

    /**
     * Get the current thread {@link ServerHttpRequest} from {@link ThreadLocal}
     *
     * @return {@link ServerHttpRequest}
     */
    public static ServerHttpRequest get(){
        return requests.get();
    }

    /**
     * Clear the current thread {@link ServerHttpRequest} from {@link ThreadLocal}
     */
    public static void reset(){
        requests.remove();
    }

    /**
     * Gets the {@code Mono<ServerHttpRequest>} from Reactor {@link }
     * @return the {@code Mono<ServerHttpRequest>}
     */
    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(Mono::just)
                .map(contextView -> contextView.get(ServerWebExchange.class).getRequest());
    }


}
