package com.somnus.microservice.cache.condition;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.cache.condition
 * @title: LockCondition
 * @description: TODO
 * @date 2019/6/14 16:43
 */
@AllArgsConstructor
public class CacheCondition implements Condition {

    private String key;

    private String value;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String beanName = context.getEnvironment().getProperty(key);

        return StringUtils.equals(beanName, value);
    }
}