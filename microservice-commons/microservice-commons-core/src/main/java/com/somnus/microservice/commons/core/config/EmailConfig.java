package com.somnus.microservice.commons.core.config;

import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.exception.BusinessException;
import com.somnus.microservice.commons.base.properties.EmailProperties;
import com.somnus.microservice.commons.base.properties.EmailProperties.Properties;
import lombok.Data;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.config
 * @title: PopoConfig
 * @description: TODO
 * @date 2020/7/10 16:59
 */
@Data
public class EmailConfig {

    private Map<String, JavaMailSender> map;

    private Map<String, String> map2;

    public EmailConfig(EmailProperties email){
        Optional.ofNullable(email.getConfigs()).ifPresent((po)->
            map = Optional.ofNullable(po)
                    .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CN10048)).stream()
                    .collect(Collectors.toMap(Properties::getOrg, v -> {
                        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
                        javaMailSender.setDefaultEncoding(v.getDefaultEncoding());
                        javaMailSender.setHost(v.getHost());
                        javaMailSender.setPort(v.getPort());
                        javaMailSender.setProtocol(v.getProtocol());
                        javaMailSender.setUsername(v.getUsername());
                        javaMailSender.setPassword(v.getPassword());

                        if(v.getPort() != 25){
                            java.util.Properties javaMailProperties = javaMailSender.getJavaMailProperties();
                            javaMailProperties.setProperty("mail.smtp.auth", "true");
                            javaMailProperties.setProperty("mail.debug", "true");//启用调试
                            javaMailProperties.setProperty("mail.smtp.timeout", "3000"); //设置超时时间3秒
                            javaMailProperties.setProperty("mail.smtp.starttls.enable", "false");
                            javaMailProperties.setProperty("mail.smtp.starttls.required", "false");
                            javaMailProperties.setProperty("mail.smtp.ssl.enable", "true");
                            javaMailProperties.setProperty("mail.smtp.ssl.trust", v.getHost());
                            javaMailProperties.setProperty("mail.imap.ssl.socketFactory.fallback", "false");
                            javaMailProperties.setProperty("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                            javaMailSender.setJavaMailProperties(javaMailProperties);
                        }

                        return javaMailSender;
                    }, (existing, replacement) -> existing))
        );
        Optional.ofNullable(email.getConfigs()).ifPresent((po)->
                map2 = Optional.ofNullable(po)
                        .orElseThrow(() -> new BusinessException(ErrorCodeEnum.CN10048)).stream()
                        .collect(Collectors.toMap(Properties::getOrg, Properties::getUsername, (existing, replacement) -> existing))
        );
    }

    public JavaMailSender getMailSender(String org){
        return map.get(org);
    }

    public String getFrom(String org){
        return map2.get(org);
    }

}
