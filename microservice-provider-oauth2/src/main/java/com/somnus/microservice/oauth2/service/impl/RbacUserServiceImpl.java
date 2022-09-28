package com.somnus.microservice.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.commons.core.support.Objects;
import com.somnus.microservice.commons.security.core.principal.UserInfo;
import com.somnus.microservice.oauth2.mapper.RbacUserMapper;
import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.model.query.UserPageQuery;
import com.somnus.microservice.oauth2.service.RbacUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author kevin.liu
 * @title: RbacUserServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/22 15:49
 */
@Service
@RequiredArgsConstructor
public class RbacUserServiceImpl extends BaseService<RbacUser> implements RbacUserService {

    private final RbacUserMapper rbacUserMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<RbacUser> getByUsername(String username) {

        return Optional.ofNullable(rbacUserMapper.selectOne(new QueryWrapper<RbacUser>().eq("username", username)));
    }

    @Override
    public Optional<RbacUser> getByMobile(String mobile) {
        return Optional.ofNullable(rbacUserMapper.selectOne(new QueryWrapper<RbacUser>().eq("mobile", mobile)));
    }

    @Override
    public void save(String username, String password) {
        rbacUserMapper.insert(new RbacUser(username, passwordEncoder.encode(password)));
    }

    @Override
    public IPage<UserInfo> selectByPage(UserPageQuery query) {

        Page<RbacUser> page = new Page<>(query.getPageNum(), query.getPageSize());

        page = rbacUserMapper.selectPage(page, new QueryWrapper<RbacUser>().eq("username", query.getUsername()));

        return Objects.convert(page, UserInfo.class);
    }

    @Override
    public PageInfo<UserInfo> selectByPage2(UserPageQuery query) {

        PageInfo<RbacUser> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(() -> rbacUserMapper.selectList(new QueryWrapper<RbacUser>().eq("username", query.getUsername())));

        return Objects.convert(pageInfo, UserInfo.class);
    }
}
