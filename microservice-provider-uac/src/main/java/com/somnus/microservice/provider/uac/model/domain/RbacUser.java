package com.somnus.microservice.provider.uac.model.domain;

import com.somnus.microservice.commons.core.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;

/**
 * @author kevin.liu
 * @title: RbacUser
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/22 15:44
 */
@Data
@Table(name = "rbac_user")
@Alias(value = "rbacUser")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RbacUser extends BaseEntity {

    public RbacUser(String username, String password) {
        this.username = username;
        this.password = password;
        super.creator();
    }

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