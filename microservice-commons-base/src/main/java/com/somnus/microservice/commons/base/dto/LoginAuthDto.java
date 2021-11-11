package com.somnus.microservice.commons.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.dto
 * @title: LoginAuthDto
 * @description: TODO
 * @date 2019/3/18 15:27
 */
@Data
@ApiModel(value = "登录人信息")
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthDto implements Serializable {

    private static final long serialVersionUID = -1137852221455042256L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "组织ID")
    private Long groupId;

    @ApiModelProperty(value = "组织名称")
    private String groupName;

    public LoginAuthDto(Long userId, String userName, String realName) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
    }
}