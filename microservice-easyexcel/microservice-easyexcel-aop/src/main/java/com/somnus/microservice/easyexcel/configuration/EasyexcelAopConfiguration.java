package com.somnus.microservice.easyexcel.configuration;

import com.somnus.microservice.easyexcel.aop.EasyexcelAutoScanProxy;
import com.somnus.microservice.easyexcel.aop.EasyexcelInterceptor;
import com.somnus.microservice.easyexcel.condition.ExcelCondition;
import com.somnus.microservice.easyexcel.constant.EasyexcelConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.configuration
 * @title: EasyexcelAopConfiguration
 * @description: TODO
 * @date 2021/7/10 16:44
 */
@Configuration
public class EasyexcelAopConfiguration {

    @Value("${" + EasyexcelConstant.EASYEXCEL_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    @Conditional(ExcelCondition.class)
    public EasyexcelAutoScanProxy easyexcelAutoScanProxy() {
        return new EasyexcelAutoScanProxy(scanPackages);
    }

    @Bean
    @Conditional(ExcelCondition.class)
    public EasyexcelInterceptor easyexcelInterceptor() {
        return new EasyexcelInterceptor();
    }
}
