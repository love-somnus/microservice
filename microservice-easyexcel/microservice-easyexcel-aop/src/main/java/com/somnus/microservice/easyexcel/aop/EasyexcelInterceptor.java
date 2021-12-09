package com.somnus.microservice.easyexcel.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.AbstractInterceptor;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author kevin.liu
 * @title: DynamicNameAspect
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 13:19
 */
public class EasyexcelInterceptor extends AbstractInterceptor {

    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ResponseExcel excelAnnotation = getExcelAnnotation(invocation);
        if (excelAnnotation != null){
            String name = excelAnnotation.name();
            // 当配置的 excel 名称为空时，取当前时间
            if (!StringUtils.hasText(name)) {
                name = LocalDateTime.now().toString();
            }
            else {
                name = getSpelKey(invocation, name);
            }
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            Objects.requireNonNull(requestAttributes).setAttribute(EXCEL_NAME_KEY, name, RequestAttributes.SCOPE_REQUEST);
        }
        return invocation.proceed();
    }

    private ResponseExcel getExcelAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(ResponseExcel.class)) {
            return method.getAnnotation(ResponseExcel.class);
        }
        return null;
    }
}
