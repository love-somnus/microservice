package com.somnus.microservice.commons.security.core.principal;

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
public class UserInfo implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

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
    private String status;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;
}
