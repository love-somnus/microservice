package com.somnus.microservice.oauth2.web.controller;

import com.somnus.microservice.commons.base.wrapper.WrapMapper;
import com.somnus.microservice.commons.base.wrapper.Wrapper;
import com.somnus.microservice.easyexcel.annotation.ResponseExcel;
import com.somnus.microservice.easyexcel.annotation.Sheet;
import com.somnus.microservice.oauth2.model.domain.DemoData;
import com.somnus.microservice.oauth2.service.RbacUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(value = "user/save")
    public Wrapper<String> save(String username, String password){

        userService.save(username, password);

        return WrapMapper.ok();
    }

    @ResponseExcel(name = "excel", password = "123456", sheets = {@Sheet(sheetName = "demoList")})
    @GetMapping("download")
    public List<DemoData> e1() {
        List<DemoData> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData data = new DemoData("tr1" + i, "tr2" + i);
            dataList.add(data);
        }
        return dataList;
    }

    @ResponseExcel(name = "excel2", password = "123456", multi = true,
            sheets = {@Sheet(sheetName = "第一个Sheet"),@Sheet(sheetName = "第二个sheet")}
            )
    @GetMapping("multi/download")
    public List<List<DemoData>> e2() {
        List<List<DemoData>> lists = new ArrayList<>();

        List<DemoData> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData data = new DemoData("tr1" + i, "tr2" + i);
            dataList.add(data);
        }

        List<DemoData> dataList2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData data = new DemoData("tr1" + i, "tr2" + i);
            dataList2.add(data);
        }

        lists.add(dataList);
        lists.add(dataList2);

        return lists;
    }

}
