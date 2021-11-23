package com.somnus.microservice.commons.core.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somnus.microservice.commons.base.dto.LoginAuthDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.mybatis
 * @title: BaseEntity
 * @description: The class Base entity.
 * @date 2019/3/15 16:30
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 2393269568666085258L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField("created_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    /**
     * 最近操作人
     */
    @TableField("last_operator")
    private String lastOperator;

    /**
     * 最后操作人ID
     */
    @TableField("last_operator_id")
    private Long lastOperatorId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * Is new boolean.
     *
     * @return the boolean
     */
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * Sets update info.
     *
     * @param user the user
     */
    @Transient
    @JsonIgnore
    public void setUpdateInfo(LoginAuthDto user) {

        if (isNew()) {
            this.creatorId = (this.lastOperatorId = user.getUserId());
            this.creator = user.getUserName();
            this.createdTime = (this.updateTime = new Date());
        }
        this.lastOperatorId = user.getUserId();
        this.lastOperator = user.getUserName() == null ? user.getRealName() : user.getUserName();
        this.updateTime = new Date();
    }

    @Transient
    @JsonIgnore
    public void creator() {
        this.creatorId = 0L;
        this.creator = "admin";
        this.createdTime = (this.updateTime = new Date());
    }
}
