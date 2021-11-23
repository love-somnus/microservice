package com.somnus.microservice.oauth2.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.oauth2.service.RbacUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kevin.liu
 * @title: AuthorizationController
 * @description: TODO
 * @date 2021/11/19 19:09
 */
@RestController
@RequestMapping("auth")
public class AuthenticateController {

    @Autowired
    private RbacUserService userService;

    @GetMapping(value = "")
    public String auth(){
        return "ok";
    }

    @PostMapping(value = "save")
    public Wrapper<String> save(String username, String password){

        userService.save(username, password);

        return WrapMapper.ok();
    }
}
