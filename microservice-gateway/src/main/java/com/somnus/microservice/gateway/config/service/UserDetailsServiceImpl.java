package com.somnus.microservice.gateway.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

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
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(
                "user",
                passwordEncoder.encode("user"),
                true, true, true, true, new ArrayList<>(),
                1L
        );
        return Mono.just(securityUserDetails);
    }


}