package com.somnus.microservice.oauth2.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.oauth2.model.query.UserPageQuery;
import com.somnus.microservice.oauth2.service.RbacUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author kevin.liu
 * @title: AuthorizationController
 * @description: TODO
 * @date 2021/11/19 19:09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticateController {

    private final RbacUserService userService;

    @GetMapping(value = "")
    public String auth(){
        return "ok";
    }

    @PostMapping(value = "user/save")
    public Wrapper<String> save(String username, String password){

        userService.save(username, password);

        return WrapMapper.ok();
    }

    @GetMapping(value = "user/selectByPage")
    public Wrapper<?> selectByPage(UserPageQuery query){

        return WrapMapper.success(userService.selectByPage(query));
    }

    @GetMapping(value = "user/selectByPage2")
    public Wrapper<?> selectByPage2(UserPageQuery query){

        return WrapMapper.success(userService.selectByPage2(query));
    }

}
