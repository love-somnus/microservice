package com.somnus.microservice.provider.uac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.provider.uac.mapper.RbacUserMapper;
import com.somnus.microservice.provider.uac.model.domain.RbacUser;
import com.somnus.microservice.provider.uac.service.RbacUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RbacUserServiceImpl extends BaseService<RbacUser> implements RbacUserService {

    @Autowired
    private RbacUserMapper rbacUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<RbacUser> getByUsername(String username) {

        return Optional.ofNullable(rbacUserMapper.selectOne(new QueryWrapper<RbacUser>().eq("username", username)));
    }

    @Override
    public void save(String username, String password) {
        rbacUserMapper.insert(new RbacUser(username, passwordEncoder.encode(password)));
    }
}
