package com.somnus.microservice.gateway.config.handler;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author kevin.liu
 * @title: DefaultServerAccessDeniedHandler
 * @projectName microservice
 * @description: 自定义未鉴权Handler
 * @date 2022/8/21 12:34
 */
@Slf4j
@Component
public class DefaultServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);

                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    DataBufferFactory dataBufferFactory = response.bufferFactory();

                    log.error("DefaultAccessDeniedHandler:{}", ErrorCodeEnum.PERMISSION_DENIED);

                    String result = JacksonUtil.toJson(WrapMapper.fail(ErrorCodeEnum.PERMISSION_DENIED));

                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));

                    return response.writeWith(Mono.just(buffer));
                });
    }
}
