package com.somnus.microservice.commons.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "登录人信息")
@NoArgsConstructor
@AllArgsConstructor
public class LoginAuthDto implements Serializable {

    private static final long serialVersionUID = -1137852221455042256L;

    @Schema(title = "用户ID")
    private Long userId;

    @Schema(title = "真实姓名")
    private String realName;

    @Schema(title = "用户名")
    private String userName;

    @Schema(title = "组织ID")
    private Long groupId;

    @Schema(title = "组织名称")
    private String groupName;

    public LoginAuthDto(Long userId, String userName, String realName) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
    }
}