package com.somnus.microservice.commons.base.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.dto
 * @title: BaseOption
 * @description: TODO
 * @date 2019/4/3 9:34
 */
@Data
@Builder
public class BaseOption implements Serializable {

    private static final long serialVersionUID = 287800651406348991L;

    private String value;

    private String label;
}
