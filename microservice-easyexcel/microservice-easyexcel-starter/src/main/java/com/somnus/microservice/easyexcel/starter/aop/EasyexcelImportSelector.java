package com.somnus.microservice.easyexcel.starter.aop;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.easyexcel.constant.EasyexcelConstant;
import com.somnus.microservice.easyexcel.starter.annotation.EnableEasyexcel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.easyexcel.starter.aop
 * @title: EasyexcelImportSelector
 * @description: TODO
 * @date 2019/7/10 17:13
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EasyexcelImportSelector extends AbstractImportSelector<EnableEasyexcel> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(EasyexcelConstant.EASYEXCEL_ENABLED, Boolean.class, Boolean.TRUE);
    }
}