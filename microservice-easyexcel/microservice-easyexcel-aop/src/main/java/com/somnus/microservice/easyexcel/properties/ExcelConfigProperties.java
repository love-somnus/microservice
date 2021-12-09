package com.somnus.microservice.easyexcel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kevin.liu
 * @title: ExcelConfigProperties
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 14:27
 */
@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

    public static final String PREFIX = "excel";

    /**
     * 模板路径
     */
    private String templatePath = "excel";

}