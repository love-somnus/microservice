package com.somnus.microservice.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.oauth2.mapper.Oauth2ClientDetailsMapper;
import com.somnus.microservice.oauth2.model.domain.Oauth2ClientDetails;
import com.somnus.microservice.oauth2.service.Oauth2ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author kevin.liu
 * @title: Oauth2ClientDetailsServiceImpl
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 20:56
 */
@Service
public class Oauth2ClientDetailsServiceImpl extends BaseService<Oauth2ClientDetails> implements Oauth2ClientDetailsService {

    @Autowired
    private Oauth2ClientDetailsMapper clientDetailsMapper;

    @Override
    public Optional<Oauth2ClientDetails> findByClientId(String clientId) {
        return Optional.ofNullable(clientDetailsMapper.selectOne(new QueryWrapper<Oauth2ClientDetails>().eq("client_id", clientId)));
    }
}
