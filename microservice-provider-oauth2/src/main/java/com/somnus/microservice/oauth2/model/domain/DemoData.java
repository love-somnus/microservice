package com.somnus.microservice.oauth2.model.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kevin.liu
 * @title: DemoData
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 19:23
 */
@Data
@AllArgsConstructor
public class DemoData {

    @ColumnWidth(10)
    @ExcelProperty("用户名")
    @ContentStyle(fillPatternType = FillPatternTypeEnum.NO_FILL, fillForegroundColor = 40)
    private String username;

    @ExcelProperty("密码")
    private String password;
}