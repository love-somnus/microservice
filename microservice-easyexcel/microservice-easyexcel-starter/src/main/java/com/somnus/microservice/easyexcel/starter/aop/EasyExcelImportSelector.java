package com.somnus.microservice.easyexcel.starter.aop;

import com.somnus.microservice.autoconfigure.selector.AbstractImportSelector;
import com.somnus.microservice.autoconfigure.selector.RelaxedPropertyResolver;
import com.somnus.microservice.easyexcel.constant.EasyExcelConstant;
import com.somnus.microservice.easyexcel.starter.annotation.EnableEasyExcel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Kevin
 * @date 2019/7/10 17:13
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EasyExcelImportSelector extends AbstractImportSelector<EnableEasyExcel> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(EasyExcelConstant.EASYEXCEL_ENABLED, Boolean.class, Boolean.TRUE);
    }
}