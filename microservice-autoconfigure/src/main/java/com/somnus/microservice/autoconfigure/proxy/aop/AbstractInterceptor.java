package com.somnus.microservice.autoconfigure.proxy.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

import com.somnus.microservice.autoconfigure.proxy.constant.ProxyConstant;
import com.somnus.microservice.autoconfigure.proxy.util.ProxyUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
/**
 * @author Kevin
 * @date 2019/6/14 9:52
 */
public abstract class AbstractInterceptor implements MethodInterceptor {

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ParameterNameDiscoverer standardReflectionParameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();

    // 通过解析字节码文件的本地变量表来获取的，只支持CGLIG(ASM library)，适用于类代理
    private final ParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public boolean isCglibAopProxy(MethodInvocation invocation) {
        return getProxyClassName(invocation).contains(ProxyConstant.CGLIB);
    }

    public String getProxyType(MethodInvocation invocation) {
        boolean isCglibAopProxy = isCglibAopProxy(invocation);
        if (isCglibAopProxy) {
            return ProxyConstant.PROXY_TYPE_CGLIB;
        } else {
            return ProxyConstant.PROXY_TYPE_REFLECTIVE;
        }
    }

    public Class<?> getProxyClass(MethodInvocation invocation) {
        return invocation.getClass();
    }

    public String getProxyClassName(MethodInvocation invocation) {
        return getProxyClass(invocation).getCanonicalName();
    }

    public Object getProxiedObject(MethodInvocation invocation) {
        return invocation.getThis();
    }

    public Class<?> getProxiedClass(MethodInvocation invocation) {
        return getProxiedObject(invocation).getClass();
    }

    public String getProxiedClassName(MethodInvocation invocation) {
        return getProxiedClass(invocation).getCanonicalName();
    }

    public Class<?>[] getProxiedInterfaces(MethodInvocation invocation) {
        return getProxiedClass(invocation).getInterfaces();
    }

    public Annotation[] getProxiedClassAnnotations(MethodInvocation invocation) {
        return getProxiedClass(invocation).getAnnotations();
    }

    public Method getMethod(MethodInvocation invocation) {
        return invocation.getMethod();
    }

    public String getMethodName(MethodInvocation invocation) {
        return getMethod(invocation).getName();
    }

    public Annotation[][] getMethodParameterAnnotations(MethodInvocation invocation) {
        return getMethod(invocation).getParameterAnnotations();
    }

    public Class<?>[] getMethodParameterTypes(MethodInvocation invocation) {
        return getMethod(invocation).getParameterTypes();
    }

    public String getMethodParameterTypesValue(MethodInvocation invocation) {
        Class<?>[] parameterTypes = getMethodParameterTypes(invocation);

        return ProxyUtil.toString(parameterTypes);
    }

    /**
     * 获取变量名
     */
    public String[] getMethodParameterNames(MethodInvocation invocation) {
        Method method = getMethod(invocation);

        boolean isCglibAopProxy = isCglibAopProxy(invocation);

        if (isCglibAopProxy) {
            return localVariableTableParameterNameDiscoverer.getParameterNames(method);
        } else {
            return standardReflectionParameterNameDiscoverer.getParameterNames(method);
        }
    }

    public Annotation[] getMethodAnnotations(MethodInvocation invocation) {
        return getMethod(invocation).getAnnotations();
    }

    public Object[] getArguments(MethodInvocation invocation) {
        return invocation.getArguments();
    }

    /**
     * 解析SpEL表达式
     * 非模板表达式: "#username"
     * 非模板表达式: "#username + \"-\" + #password"
     * 模板表达式:"#{#username}-#{#password}"
     *
     * @param invocation  方法
     * @param condition   表达式
     * @param returnClass 返回类型
     * @param template    是否启用模板
     * @param <T>         类型
     * @return T
     */
    private <T> T parse(MethodInvocation invocation, String condition, Class<T> returnClass, boolean template) {
        String[] parameterNames = getMethodParameterNames(invocation);

        Object[] arguments = getArguments(invocation);

        // SPEL上下文
        EvaluationContext context = new StandardEvaluationContext();

        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }
        if (template) {
            return parser.parseExpression(condition, ParserContext.TEMPLATE_EXPRESSION).getValue(context, returnClass);
        } else {
            return parser.parseExpression(condition).getValue(context, returnClass);
        }
    }

    public String getSpelKey(MethodInvocation invocation, String condition) {

        String spelKey = parse(invocation, condition, String.class, true);

        if(Objects.equals(spelKey, condition)){
            return parse(invocation, condition, String.class, false);
        }

        return spelKey;
    }

}
