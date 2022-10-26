package com.somnus.microservice.easyexcel.starter.configuration;

import com.somnus.microservice.easyexcel.configuration.EasyExcelAopConfiguration;
import com.somnus.microservice.easyexcel.webmvc.configuration.ExcelHandlerConfiguration;
import com.somnus.microservice.easyexcel.webmvc.configuration.ResponseExcelAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @date 2019/7/10 17:14
 */
@Configuration
@Import({ EasyExcelAopConfiguration.class, ExcelHandlerConfiguration.class, ResponseExcelAutoConfiguration.class })
public class EasyExcelConfiguration {

}