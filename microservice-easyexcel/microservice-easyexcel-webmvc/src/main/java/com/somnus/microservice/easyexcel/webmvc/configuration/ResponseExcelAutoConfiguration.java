package com.somnus.microservice.easyexcel.webmvc.configuration;

import com.somnus.microservice.easyexcel.properties.ExcelConfigProperties;
import com.somnus.microservice.easyexcel.webmvc.resolver.RequestExcelArgumentResolver;
import com.somnus.microservice.easyexcel.webmvc.resolver.ResponseExcelReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin.liu
 * @title: ResponseExcelAutoConfiguration
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 13:17
 */
@RequiredArgsConstructor
@Import(ExcelHandlerConfiguration.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExcelConfigProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResponseExcelAutoConfiguration {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

    /**
     * 追加 Excel 请求处理器 到 SpringMVC 中
     */
    @PostConstruct
    public void setRequestExcelArgumentResolver() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();

        List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
        resolverList.add(new RequestExcelArgumentResolver());
        assert argumentResolvers != null;
        resolverList.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
    }

    /**
     * 追加 Excel返回值处理器 到 SpringMVC 中
     */
    @PostConstruct
    public void setReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();

        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
        newHandlers.add(responseExcelReturnValueHandler);
        assert returnValueHandlers != null;
        newHandlers.addAll(returnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }

}
