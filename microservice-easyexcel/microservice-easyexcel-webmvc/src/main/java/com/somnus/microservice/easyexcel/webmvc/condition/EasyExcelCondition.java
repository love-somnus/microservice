package com.somnus.microservice.easyexcel.webmvc.condition;

import com.somnus.microservice.easyexcel.condition.ExcelCondition;
import com.somnus.microservice.easyexcel.constant.EasyExcelConstant;

/**
 * @author kevin.liu
 * @date 2022/10/25 10:00
 */
public class EasyExcelCondition extends ExcelCondition {

    public EasyExcelCondition() {
        super(EasyExcelConstant.EASYEXCEL_TYPE, EasyExcelConstant.EASYEXCEL_TYPE_MVC);
    }
}
