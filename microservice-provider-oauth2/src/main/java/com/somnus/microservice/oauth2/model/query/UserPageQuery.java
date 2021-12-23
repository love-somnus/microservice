package com.somnus.microservice.oauth2.model.query;

import com.somnus.microservice.commons.base.dto.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kevin.liu
 * @title: UserPageQuery
 * @projectName microservice
 * @description: TODO
 * @date 2021/12/20 13:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageQuery extends BaseQuery {

    private String username;
}
