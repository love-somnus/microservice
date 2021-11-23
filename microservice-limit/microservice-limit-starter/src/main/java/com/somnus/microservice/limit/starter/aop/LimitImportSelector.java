package com.somnus.microservice.limit.starter.aop;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.starter.annotation.EnableLimit;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.starter.aop
 * @title: LimitImportSelector
 * @description: TODO
 * @date 2019/7/10 17:13
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class LimitImportSelector extends AbstractImportSelector<EnableLimit> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(LimitConstant.LIMIT_ENABLED, Boolean.class, Boolean.TRUE);
    }
}