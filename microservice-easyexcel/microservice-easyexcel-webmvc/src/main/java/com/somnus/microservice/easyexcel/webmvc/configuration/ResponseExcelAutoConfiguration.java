package com.somnus.microservice.easyexcel.webmvc.configuration;

import com.somnus.microservice.easyexcel.configuration.EasyExcelAopConfiguration;
import com.somnus.microservice.easyexcel.webmvc.resolver.RequestExcelArgumentResolver;
import com.somnus.microservice.easyexcel.webmvc.resolver.ResponseExcelReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author kevin.liu
 * @title: ResponseExcelAutoConfiguration
 * @date 2021/12/9 13:17
 */
@RequiredArgsConstructor
@AutoConfigureBefore(EasyExcelAopConfiguration.class)
@ConditionalOnProperty(prefix = "easyexcel",value = "enabled",havingValue = "true")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResponseExcelAutoConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    /**
     * 追加 Excel 请求处理器 到 SpringMVC 中
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = Objects.requireNonNull(requestMappingHandlerAdapter.getArgumentResolvers());

        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        resolverList.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }

    /**
     * 追加 Excel返回值处理器 到 SpringMVC 中
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = Objects.requireNonNull(requestMappingHandlerAdapter.getReturnValueHandlers());

        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(responseExcelReturnValueHandler);
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

}
