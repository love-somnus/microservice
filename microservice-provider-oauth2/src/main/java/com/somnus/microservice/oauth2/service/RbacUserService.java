package com.somnus.microservice.oauth2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.model.query.UserPageQuery;
import com.somnus.microservice.oauth2.model.vo.RbacUserVo;

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

    void save(String username, String password);

    IPage<RbacUserVo> selectByPage(UserPageQuery query);

    PageInfo<RbacUserVo> selectByPage2(UserPageQuery query);
}
