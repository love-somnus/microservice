package com.somnus.microservice.xxljob.autoconfigure;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.somnus.microservice.commons.base.utils.JacksonUtil;
import com.somnus.microservice.xxljob.constants.Wrapper;
import com.somnus.microservice.xxljob.service.XxlJobService;
import com.somnus.microservice.xxljob.service.impl.XxlJobServiceImpl;
import com.somnus.microservice.xxljob.response.HttpHeader;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.HttpCookie;
import java.util.Map;

/**
 * @author kevin.liu
 * @title: XxlJobAutoConfiguration
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 14:29
 */
@Slf4j
/*@Configuration*/
@RequiredArgsConstructor
@ConditionalOnClass(XxlJobService.class)
@EnableConfigurationProperties({XxlJobProperties.class})
public class XxlJobAutoConfiguration {

    private final XxlJobProperties properties;

    @Bean
    @ConditionalOnProperty(prefix = "xxl.job",value = "enable",havingValue = "true")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(properties.getAccessToken());
        xxlJobSpringExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(properties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(properties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(properties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(properties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(properties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(properties.getExecutor().getLogretentiondays());

        return xxlJobSpringExecutor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "xxl.job",value = "enable",havingValue = "true")
    public XxlJobService xxlJobService(HttpHeader loginHeader, XxlJobProperties properties) {
        return new XxlJobServiceImpl(loginHeader, properties);
    }

    @Bean("loginHeader")
    @ConditionalOnProperty(prefix = "xxl.job",value = "enable",havingValue = "true")
    public HttpHeader httpRequest(XxlJobProperties properties){
        String adminUrl = properties.getAdmin().getAddresses();
        String userName = properties.getAdmin().getUsername();
        String password = properties.getAdmin().getPassword();
        int connectionTimeOut = properties.getAdmin().getConnectionTimeOut();

        Map<String, Object > paramMap = ImmutableMap.of("userName", userName, "password", password);

        HttpResponse httpResponse = HttpRequest.post(adminUrl+"/login").form(paramMap).timeout(connectionTimeOut).execute();

        int status = httpResponse.getStatus();

        if (200 != status) {
            throw new RuntimeException("登录失败");
        }

        String body = httpResponse.body();

        Wrapper<String> wrapper = JacksonUtil.parseJson(body, new TypeReference<Wrapper<String>>(){});

        if (200 != wrapper.getCode()) {
            throw new RuntimeException("登录失败:" + wrapper.getMsg());
        }

        String cookieName = "XXL_JOB_LOGIN_IDENTITY";
        HttpCookie cookie = httpResponse.getCookie(cookieName);
        if (cookie == null) {
            throw new RuntimeException("没有获取到登录成功的cookie，请检查登录连接或者参数是否正确");
        }
        String headerValue = cookieName + "=" + cookie.getValue();

        return new HttpHeader("Cookie", headerValue);
    }

}
