package com.somnus.microservice.provider.uac.config.handler;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.commons.base.wrapper.WrapMapper;
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
 * @title: DefaultAccessDeniedHandler
 * @projectName uac
 * @description: 自定义鉴权失败Handler
 * @date 2021/11/15 15:11
 */
@Component
public class DefaultAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);

                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    DataBufferFactory dataBufferFactory = response.bufferFactory();

                    String result = JacksonUtil.toJson(WrapMapper.fail(ErrorCodeEnum.PERMISSION_DENIED));

                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));

                    return response.writeWith(Mono.just(buffer));
                });
    }
}
