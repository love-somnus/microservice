package com.somnus.microservice.xxljob.constants;

import lombok.Getter;

/**
 * @author kevin.liu
 * @title: GlueTypeEnum
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:46
 */
@Getter
public enum GlueTypeEnum {
    BEAN("BEAN"),
    GLUE_GROOVY("GLUE(Java)"),
    GLUE_SHELL("GLUE(Shell)"),
    GLUE_PYTHON("GLUE(Python)"),
    GLUE_PHP("GLUE(PHP)"),
    GLUE_NODEJS("GLUE(Nodejs)"),
    GLUE_POWERSHELL("GLUE(PowerShell)"),
    ;
    private String remark;

    GlueTypeEnum(String remark) {
        this.remark = remark;
    }
}