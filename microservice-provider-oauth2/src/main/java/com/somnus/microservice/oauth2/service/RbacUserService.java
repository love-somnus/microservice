package com.somnus.microservice.oauth2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.commons.security.core.principal.UserInfo;
import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.model.query.UserPageQuery;

import java.util.Optional;

/**
 * @author kevin.liu
 * @title: RbacUserService
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/22 15:47
 */
public interface RbacUserService extends IService<RbacUser> {

    Optional<RbacUser> getByUsername(String username);

    Optional<RbacUser> getByMobile(String mobile);

    void save(String username, String password);

    IPage<UserInfo> selectByPage(UserPageQuery query);

    PageInfo<UserInfo> selectByPage2(UserPageQuery query);
}
