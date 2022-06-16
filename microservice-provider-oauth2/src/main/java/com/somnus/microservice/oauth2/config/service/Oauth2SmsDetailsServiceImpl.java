package com.somnus.microservice.oauth2.config.service;

import cn.hutool.core.util.StrUtil;
import com.somnus.microservice.commons.security.core.constant.SecurityConstants;
import com.somnus.microservice.commons.security.core.principal.Oauth2User;
import com.somnus.microservice.commons.security.service.Oauth2UserDetailsService;
import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.service.RbacUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

/**
 * @author kevin.liu
 * @title: Oauth2SmsDetailsServiceImpl
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/14 19:14
 */
public class Oauth2SmsDetailsServiceImpl implements Oauth2UserDetailsService {

    @Autowired
    private RbacUserService userService;

    @Override
    public UserDetails loadUserByUsername(String mobile) {

        RbacUser user = userService.getByMobile(mobile).orElseThrow(() -> new UsernameNotFoundException("mobile: [" + mobile + "] do not exist!"));

        // 获取用户角色(暂时不获取)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("admin"));

        /*Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));*/

        // 构造security用户
        return new Oauth2User(user.getId(), user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(), user.getMobile(), true, true, true,
                StrUtil.equals(user.getStatus(), SecurityConstants.STATUS_NORMAL), authorities);
    }

    @Override
    public UserDetails loadUserByUser(Oauth2User user) {
        return this.loadUserByUsername(user.getMobile());
    }

    /**
     * 是否支持此客户端校验
     * @param clientId 目标客户端
     * @return true/false
     */
    @Override
    public boolean support(String clientId, String grantType) {
        return SecurityConstants.SMS.equals(clientId);
    }

}
