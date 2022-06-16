package com.somnus.microservice.commons.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.principal.Oauth2User;
import com.somnus.microservice.commons.security.core.principal.UserInfo;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface Oauth2UserDetailsService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 通过用户实体查询
     * @param Oauth2User user
     * @return
     */
    default UserDetails loadUserByUser(Oauth2User Oauth2User) {
        return this.loadUserByUsername(Oauth2User.getUsername());
    }

}