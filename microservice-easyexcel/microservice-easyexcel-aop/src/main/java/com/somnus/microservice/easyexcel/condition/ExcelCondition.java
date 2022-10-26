package com.somnus.microservice.easyexcel.condition;

import com.somnus.microservice.autoconfigure.proxy.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * @author kevin.liu
 * @date 2022/9/14 13:16
 */
@RequiredArgsConstructor
public class ExcelCondition implements Condition {

    private final String key;

    private final String value;

    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        String beanName = context.getEnvironment().getProperty(key);

        return Objects.equals(beanName, value);
    }
}