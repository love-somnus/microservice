package com.somnus.microservice.commons.base.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.dto
 * @title: BaseModel
 * @description: TODO
 * @date 2020/9/9 9:50
 */
@Data
public class BaseModel implements Serializable {

    private static final long serialVersionUID = -8215576699979557657L;

    @ExcelIgnore
    private String sheet;
}
