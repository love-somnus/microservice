package com.somnus.microservice.lock.starter.aop;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.lock.constant.LockConstant;
import com.somnus.microservice.lock.starter.annotation.EnableLock;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.lock.starter.aop
 * @title: LockImportSelector
 * @description: TODO
 * @date 2019/6/14 17:26
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class LockImportSelector extends AbstractImportSelector<EnableLock> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(LockConstant.LOCK_ENABLED, Boolean.class, Boolean.TRUE);
    }

}