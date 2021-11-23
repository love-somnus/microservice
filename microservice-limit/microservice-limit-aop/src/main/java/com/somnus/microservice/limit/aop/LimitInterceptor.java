package com.somnus.microservice.limit.aop;

import com.somnus.microservice.autoconfigure.proxy.aop.AbstractInterceptor;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.core.utils.RequestUtil;
import com.somnus.microservice.limit.LimitDelegate;
import com.somnus.microservice.limit.annotation.Limit;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.aop
 * @title: LimitInterceptor
 * @description: TODO
 * @date 2019/7/10 16:37
 */
@Slf4j
public class LimitInterceptor extends AbstractInterceptor {

    @Autowired
    private LimitDelegate limitDelegate;

    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Limit limitAnnotation = getLimitAnnotation(invocation);
        if (limitAnnotation != null) {
            String name = limitAnnotation.name();
            String key = limitAnnotation.key();
            int limitPeriod = limitAnnotation.limitPeriod();
            int limitCount = limitAnnotation.limitCount();
            boolean restrictIp = limitAnnotation.restrictIp();

            return invoke(invocation, limitAnnotation, name, key, limitPeriod, limitCount, restrictIp);
        }

        return invocation.proceed();
    }

    private Limit getLimitAnnotation(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Limit.class)) {
            return method.getAnnotation(Limit.class);
        }

        return null;
    }

    private Object invoke(MethodInvocation invocation, Annotation annotation, String name, String key, int limitPeriod, int limitCount, boolean restrictIp) throws Throwable {
        if (StringUtils.isEmpty(name)) {
            throw new LimitException("Annotation [Limit]'s name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LimitException("Annotation [Limit]'s key is null or empty");
        }

        String spelKey = getSpelKey(invocation, key);
        String compositeKey = KeyUtil.getCompositeKey(prefix, name, spelKey);
        String proxyType = getProxyType(invocation);
        String proxiedClassName = getProxiedClassName(invocation);
        String methodName = getMethodName(invocation);

        if (frequentLogPrint) {
            log.info("Intercepted for annotation - Limit [key={}, limitPeriod={}, limitCount={}, proxyType={}, proxiedClass={}, method={}]", compositeKey, limitPeriod, limitCount, proxyType, proxiedClassName, methodName);
        }

        if(restrictIp){
            String ip = RequestUtil.getRemoteAddr();
            compositeKey = compositeKey + "#" + ip;
        }

        return limitDelegate.invoke(invocation, compositeKey, limitPeriod, limitCount);
    }
}