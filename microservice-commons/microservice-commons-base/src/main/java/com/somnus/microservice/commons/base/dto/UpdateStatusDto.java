package com.somnus.microservice.commons.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.dto
 * @title: UpdateStatusDto
 * @description: TODO
 * @date 2019/4/16 17:39
 */
@Data
@Schema(title = "更改状态")
public class UpdateStatusDto implements Serializable {
    private static final long serialVersionUID = 1494899235149813850L;
    /**
     * 角色ID
     */
    @Schema(title = "角色ID", required = true, example = "1")
    private Long id;

    /**
     * 状态
     */
    @Schema(title = "状态", required = true, example = "1")
    private Integer status;
}
