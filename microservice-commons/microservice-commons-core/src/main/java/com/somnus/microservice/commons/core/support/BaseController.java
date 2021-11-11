package com.somnus.microservice.commons.core.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.somnus.microservice.commons.base.dto.BaseModel;
import com.somnus.microservice.commons.core.utils.RequestUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.support
 * @title: BaseController
 * @description: TODO
 * @date 2019/4/16 18:11
 */
@Slf4j
public class BaseController {

    @SneakyThrows
    protected <T> void downloadExcel(String sheetName, Class<T> clazz, List<T> list){

        String filename = LocalDate.now().toString();

        downloadExcel(filename, sheetName, clazz, list);
    }

    @SneakyThrows
    protected <T> void downloadExcel(String filename, String sheetName, Class<T> clazz, List<T> list){

        HttpServletResponse response = RequestUtil.getResponse();

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        response.setCharacterEncoding("utf-8");

        response.setHeader("Content-disposition", "attachment;filename="+ filename+ ".xlsx");

        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
    }

    @SneakyThrows
    protected <T extends BaseModel> void downloadExcel(List<String> sheets, Class<T> clazz, List<T> list){
        HttpServletResponse response = RequestUtil.getResponse();

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        response.setCharacterEncoding("utf-8");

        String filename = LocalDate.now().toString();

        response.setHeader("Content-disposition", "attachment;filename="+ filename+ ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        Stream.iterate(0, i -> i + 1).limit(sheets.size()).forEach(i -> {

            WriteSheet writeSheet = EasyExcel.writerSheet(i, sheets.get(i)).head(clazz).build();

            List fliterlist = list.stream().filter(value -> sheets.get(i).equals(value.getSheet())).collect(Collectors.toList());

            excelWriter.write(fliterlist, writeSheet);
        });

        excelWriter.finish();
    }

    @SneakyThrows
    protected  void downloadExcel(String sheetName, List<List<String>> heads, List<List<Object>> datas){
        HttpServletResponse response = RequestUtil.getResponse();

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

        response.setCharacterEncoding("utf-8");

        String filename = LocalDate.now().toString();

        response.setHeader("Content-disposition", "attachment;filename="+ filename+ ".xlsx");

        EasyExcel.write(response.getOutputStream()).head(heads).sheet(sheetName).doWrite(datas);
    }


}
