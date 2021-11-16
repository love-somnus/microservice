package com.somnus.microservice.gateway.config.handler;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.Charset;
/**
 * @author kevin.liu
 * @title: DefaultAuthenticationEntryPoint
 * @projectName gateway
 * @description: 自定义未认证Handler
 * @date 2021/11/15 15:15
 */
@Component
public class DefaultAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return Mono.defer(() -> Mono.just(exchange.getResponse())).flatMap(response -> {

            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            DataBufferFactory dataBufferFactory = response.bufferFactory();

            String result = JacksonUtil.toJson(WrapMapper.fail(ErrorCodeEnum.USER_UNAUTHORIZED));

            DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));

            return response.writeWith(Mono.just(buffer));
        });
    }
}
