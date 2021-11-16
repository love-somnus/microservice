package com.somnus.microservice.gateway.config.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kevin.liu
 * @title: UserDetailsServiceImpl
 * @projectName gateway
 * @description: 用户登录处理
 * @date 2021/11/15 15:18
 */
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        List<String> roles = Lists.newArrayList("a","b","c");

        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityUserDetails securityUserDetails = new SecurityUserDetails(
                "user",
                passwordEncoder.encode("password"),
                true, true, true, true,
                authorities,
                "真实姓名"
        );
        return Mono.just(securityUserDetails);
    }


}