package com.somnus.microservice.oauth2.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kevin.liu
 * @title: AuthorizationController
 * @projectName microservice-elastic-job-starter
 * @description: TODO
 * @date 2021/11/19 19:09
 */
@RestController
@RequestMapping("auth")
public class AuthenticateController {

    @GetMapping(value = "")
    public String auth(){
        return "ok";
    }
}
