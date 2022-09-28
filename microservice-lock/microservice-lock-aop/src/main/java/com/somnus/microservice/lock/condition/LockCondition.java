package com.somnus.microservice.lock.condition;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.condition
 * @title: LockCondition
 * @description: TODO
 * @date 2019/6/14 16:43
 */
@RequiredArgsConstructor
public class LockCondition implements Condition {

    private final String key;

    private final String value;

    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        String beanName = context.getEnvironment().getProperty(key);

        return StringUtils.equals(beanName, value);
    }
}