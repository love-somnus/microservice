package com.somnus.microservice.gateway.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.gateway.web.controller
 * @title: IndexController
 * @description: TODO
 * @date 2019/4/22 17:44
 */
@RestController
@RequestMapping(value = "health")
public class HealthController {
    /**
     * 健康检查
     * @return
     */
    @GetMapping("")
    public String success(){
        return "200";
    }
}
