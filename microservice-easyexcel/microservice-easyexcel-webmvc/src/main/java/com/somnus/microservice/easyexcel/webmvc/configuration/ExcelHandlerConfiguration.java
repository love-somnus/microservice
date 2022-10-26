package com.somnus.microservice.easyexcel.webmvc.configuration;

import com.alibaba.excel.converters.Converter;
import com.somnus.microservice.easyexcel.webmvc.condition.EasyExcelCondition;
import com.somnus.microservice.easyexcel.webmvc.enhance.DefaultWriterBuilderEnhancer;
import com.somnus.microservice.easyexcel.webmvc.enhance.WriterBuilderEnhancer;
import com.somnus.microservice.easyexcel.webmvc.handler.MultiSheetWriteHandler;
import com.somnus.microservice.easyexcel.webmvc.handler.SheetWriteHandler;
import com.somnus.microservice.easyexcel.webmvc.handler.SingleSheetWriteHandler;
import com.somnus.microservice.easyexcel.head.I18nHeaderCellWriteHandler;
import com.somnus.microservice.easyexcel.properties.ExcelConfigProperties;
import com.somnus.microservice.easyexcel.webmvc.resolver.ResponseExcelReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author kevin.liu
 * @date 2021/12/9 13:16
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ExcelConfigProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ExcelHandlerConfiguration {

    private final ExcelConfigProperties configProperties;

    private final ObjectProvider<List<Converter<?>>> converterProvider;

    /**
     * ExcelBuild增强
     * @return DefaultWriterBuilderEnhancer 默认什么也不做的增强器
     */
    @Bean
    @ConditionalOnMissingBean
    public WriterBuilderEnhancer writerBuilderEnhancer() {
        return new DefaultWriterBuilderEnhancer();
    }

    /**
     * 单sheet 写入处理器
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(EasyExcelCondition.class)
    public SingleSheetWriteHandler singleSheetWriteHandler(I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler) {
        return new SingleSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer(), i18nHeaderCellWriteHandler);
    }

    /**
     * 多sheet 写入处理器
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(EasyExcelCondition.class)
    public MultiSheetWriteHandler manySheetWriteHandler(I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler) {
        return new MultiSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer(), i18nHeaderCellWriteHandler);
    }

    /**
     * 返回Excel文件的 response 处理器
     * @param sheetWriteHandlerList 页签写入处理器集合
     * @return ResponseExcelReturnValueHandler
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(EasyExcelCondition.class)
    public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlerList) {
        return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
    }

    /**
     * excel 头的国际化处理器
     * @param messageSource 国际化源
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MessageSource.class)
    public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
        return new I18nHeaderCellWriteHandler(messageSource);
    }

}
