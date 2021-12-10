package com.somnus.microservice.easyexcel.starter.configuration;

import com.somnus.microservice.easyexcel.configuration.EasyexcelAopConfiguration;
import com.somnus.microservice.easyexcel.webmvc.configuration.ResponseExcelAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.easyexcel.starter.configuration
 * @title: EasyexcelConfiguration
 * @description: TODO
 * @date 2019/7/10 17:14
 */
@Configuration
@Import({ EasyexcelAopConfiguration.class, ResponseExcelAutoConfiguration.class })
public class EasyexcelConfiguration {

}