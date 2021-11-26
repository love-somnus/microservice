package com.somnus.microservice.provider.cpc.service;

import com.somnus.microservice.commons.core.support.IService;
import com.somnus.microservice.provider.cpc.model.domain.SmsRecord;

public interface SmsService extends IService<SmsRecord> {

    /**
     * 发送游戏预约短信验证码
     * @param mobile
     * @param validateCode
     * @return
     */
    String sendSmsCode(String mobile, String validateCode);
}
