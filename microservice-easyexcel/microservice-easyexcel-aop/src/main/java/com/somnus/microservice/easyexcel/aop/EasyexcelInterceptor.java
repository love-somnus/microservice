package com.somnus.microservice.easyexcel.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.AbstractInterceptor;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import com.somnus.microservice.easyexcel.context.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author kevin.liu
 * @title: DynamicNameAspect
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 13:19
 */
@Slf4j
public class EasyexcelInterceptor extends AbstractInterceptor {

    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        ResponseExcel excelAnnotation = getExcelAnnotation(invocation);
        if (excelAnnotation != null){
            String name = excelAnnotation.name();
            // 当配置的 excel 名称为空时，取当前时间
            if (!StringUtils.hasText(name)) {
                name = LocalDateTime.now().toString();
            }
            else {
                try {
                    name = getSpelKey(invocation, name);
                } catch (Exception e) {
                    log.warn("key: {} is not SPEL language", name);
                }
            }
            ThreadLocalContext.set(EXCEL_NAME_KEY, name);
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
