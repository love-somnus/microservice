package com.somnus.microservice.easyexcel.configuration;

import com.somnus.microservice.easyexcel.aop.EasyExcelAutoScanProxy;
import com.somnus.microservice.easyexcel.aop.EasyExcelInterceptor;
import com.somnus.microservice.easyexcel.constant.EasyExcelConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author Kevin
 * @date 2021/7/10 16:44
 */
@ConditionalOnProperty(prefix = "easyexcel",value = "enabled",havingValue = "true")
public class EasyExcelAopConfiguration {

    @Value("${" + EasyExcelConstant.EASYEXCEL_SCAN_PACKAGES + ":}")
    private String scanPackages;

    @Bean
    public EasyExcelAutoScanProxy easyexcelAutoScanProxy() {
        return new EasyExcelAutoScanProxy(scanPackages);
    }

    @Bean
    public EasyExcelInterceptor easyexcelInterceptor() {
        return new EasyExcelInterceptor();
    }
}
