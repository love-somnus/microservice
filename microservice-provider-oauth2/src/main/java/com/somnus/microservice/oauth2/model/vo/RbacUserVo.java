package com.somnus.microservice.oauth2.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kevin.liu
 * @title: RbacUserVo
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/20 13:55
 */
@Data
public class RbacUserVo implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 状态（0:启用 1:禁用）
     */
    private Integer status;
}
