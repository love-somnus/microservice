package com.somnus.microservice.oauth2.web.controller.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author kevin.liu
 * @title: OauthTokenEndpoint
 * @projectName microservice
 * @description: TODO
 * @date 2022/6/27 17:30
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/token")
public class OauthTokenEndpoint {

    /**
     * 认证页面
     * @param model
     * @param error 表单登录失败处理回调的错误信息
     * @return ModelAndView
     */
    /*@GetMapping("/login")
    public String require(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error", error);
        return "login";
    }*/
}
