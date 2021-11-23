package com.somnus.microservice.oauth2.config.service;

import com.somnus.microservice.oauth2.model.domain.RbacUser;
import com.somnus.microservice.oauth2.service.RbacUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author kevin.liu
 * @title: UserDetailsServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/22 15:36
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RbacUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<RbacUser> optional = userService.getByUsername(username);

        optional.orElseThrow(() -> new UsernameNotFoundException("username: [" + username + "] do not exist!"));

        // 获取用户角色(暂时不获取)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("admin"));

        return User.withUsername(optional.get().getUsername())
                .password(optional.get().getPassword())
                .authorities(authorities)
                .build();
    }
}

