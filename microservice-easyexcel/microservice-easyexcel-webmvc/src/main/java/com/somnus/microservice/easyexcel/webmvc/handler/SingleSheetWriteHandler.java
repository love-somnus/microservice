package com.somnus.microservice.easyexcel.webmvc.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;

import com.somnus.microservice.easyexcel.webmvc.enhance.WriterBuilderEnhancer;
import com.somnus.microservice.easyexcel.properties.ExcelConfigProperties;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @author kevin.liu
 * @title: SingleSheetWriteHandler
 * @projectName microservice
 * @description: 处理单sheet 页面
 * @date 2021/12/9 11:39
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

    public SingleSheetWriteHandler(ExcelConfigProperties configProperties, ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer enhance) {
        super(configProperties, converterProvider, enhance);
    }

    /**
     * obj 是List 且list不为空同时list中的元素不是是List 才返回true
     * @param obj 返回对象
     * @return
     */
    @Override
    public boolean support(ResponseExcel obj) {
        return ! obj.multi();
    }

    @Override
    @SneakyThrows
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List list = (List) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        // 有模板则不指定sheet名
        Class<?> dataClass = list.get(0).getClass();
        WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(), responseExcel.headGenerator());
        excelWriter.write(list, sheet);
        excelWriter.finish();
    }

}