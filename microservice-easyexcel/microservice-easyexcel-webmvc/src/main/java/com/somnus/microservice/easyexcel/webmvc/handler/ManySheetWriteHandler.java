package com.somnus.microservice.easyexcel.webmvc.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.somnus.microservice.easyexcel.webmvc.enhance.WriterBuilderEnhancer;
import com.somnus.microservice.easyexcel.exception.ExcelException;
import com.somnus.microservice.easyexcel.properties.ExcelConfigProperties;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import com.somnus.microservice.easyexcel.annotation.Sheet;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author kevin.liu
 * @title: ManySheetWriteHandler
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/9 11:38
 */
public class ManySheetWriteHandler extends AbstractSheetWriteHandler {

    public ManySheetWriteHandler(ExcelConfigProperties configProperties, ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer enhance) {
        super(configProperties, converterProvider, enhance);
    }

    /**
     * 当且仅当List不为空且List中的元素也是List 才返回true
     * @param obj 返回对象
     * @return
     */
    @Override
    public boolean support(Object obj) {
        if (obj instanceof List) {
            List objList = (List) obj;
            return !objList.isEmpty() && objList.get(0) instanceof List;
        }
        else {
            throw new ExcelException("@ResponseExcel 返回值必须为List类型");
        }
    }

    @Override
    @SneakyThrows
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List objList = (List) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        Sheet[] sheets = responseExcel.sheets();
        WriteSheet sheet;
        for (int i = 0; i < sheets.length; i++) {
            List eleList = (List) objList.get(i);
            Class<?> dataClass = eleList.get(0).getClass();
            // 创建sheet
            sheet = this.sheet(sheets[i], dataClass, responseExcel.template(), responseExcel.headGenerator());
            // 写入sheet
            excelWriter.write(eleList, sheet);
        }
        excelWriter.finish();
    }

}
