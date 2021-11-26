package com.somnus.microservice.provider.cpc.service.impl;

import com.somnus.microservice.commons.core.support.BaseService;
import com.somnus.microservice.provider.cpc.model.domain.SmsRecord;
import com.somnus.microservice.provider.cpc.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author kevin.liu
 * @title: SmsServiceImpl
 * @projectName SpringBoot
 * @description: TODO
 * @date 2021/11/25 19:09
 */
@Slf4j
@Service
public class SmsServiceImpl extends BaseService<SmsRecord> implements SmsService {

    @Override
    public String sendSmsCode(String mobile, String validateCode) {

        try {
            log.info("发送短信验证码 {}：{}", mobile ,mobile);
            TimeUnit.MILLISECONDS.sleep(500L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RandomStringUtils.randomNumeric(6);
    }
}
