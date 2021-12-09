package com.somnus.microservice.easyexcel.webmvc.resolver;

import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import com.somnus.microservice.easyexcel.webmvc.handler.SheetWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kevin.liu
 * @title: ResponseExcelReturnValueHandler
 * @projectName microservice
 * @description: 处理@ResponseExcel 返回值
 * @date 2021/12/9 13:21
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final List<SheetWriteHandler> sheetWriteHandlerList;

    /**
     * 只处理@ResponseExcel 声明的方法
     * @param parameter 方法签名
     * @return 是否处理
     */
    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.getMethodAnnotation(ResponseExcel.class) != null;
    }

    /**
     * 处理逻辑
     * @param o 返回参数
     * @param parameter 方法签名
     * @param mavContainer 上下文容器
     * @param nativeWebRequest 上下文
     */
    @Override
    public void handleReturnValue(Object o, MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest nativeWebRequest) {

        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");

        ResponseExcel responseExcel = parameter.getMethodAnnotation(ResponseExcel.class);
        Assert.state(responseExcel != null, "No @ResponseExcel");

        mavContainer.setRequestHandled(true);

        sheetWriteHandlerList.stream()
                .filter(handler -> handler.support(responseExcel))
                .findFirst()
                .ifPresent(handler -> handler.export(o, response, responseExcel));
    }

}