package com.somnus.microservice.oauth2.config.service;

import cn.hutool.core.util.StrUtil;
import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.principal.Oauth2User;
import com.somnus.microservice.commons.security.service.Oauth2UserDetailsService;
import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.service.RbacUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

/**
 * @author kevin.liu
 * @title: Oauth2UserDetailsServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/22 15:36
 */
@RequiredArgsConstructor
public class Oauth2UserDetailsServiceImpl implements Oauth2UserDetailsService {

    private final RbacUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RbacUser user = userService.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("mobile: [" + username + "] do not exist!"));

        // 获取用户角色(暂时不获取)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("admin"));

        /*Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));*/

        // 构造security用户
        return new Oauth2User(user.getId(), user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(), user.getMobile(), true, true, true,
                StrUtil.equals(user.getStatus(), SecurityConstants.STATUS_NORMAL), authorities);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

