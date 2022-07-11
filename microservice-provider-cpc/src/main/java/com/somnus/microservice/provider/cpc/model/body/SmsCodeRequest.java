package com.somnus.microservice.provider.cpc.model.body;

import lombok.Data;

/**
 * @author kevin.liu
 * @title: SmsCodeRequest
 * @projectName microservice
 * @description: TODO
 * @date 2022/7/4 17:40
 */
@Data
public class SmsCodeRequest{

    private String mobile;

    private String validateCode;
}