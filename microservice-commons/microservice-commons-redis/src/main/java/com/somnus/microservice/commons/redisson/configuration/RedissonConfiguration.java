package com.somnus.microservice.commons.redisson.configuration;

import com.google.common.collect.ImmutableMap;
import com.somnus.microservice.commons.redisson.constant.RedissonConstant;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.commons.redisson.handler.RedissonHandlerImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redisson.configuration
 * @title: RedissonConfiguration
 * @description: http://192.168.95.41:8848/nacos/v1/cs/configs?dataId=redisson.yaml&group=DEFAULT_GROUP
 * @date 2019/7/5 16:42
 */
@Configuration
@RequiredArgsConstructor
public class RedissonConfiguration {

    @Value("${" + RedissonConstant.NACOS_URL + "}")
    private String nacosUrl;

    @Value("${" + RedissonConstant.PATH + ":" + RedissonConstant.DEFAULT_PATH + "}")
    private String redissonPath;

    @Bean
    @SneakyThrows(Exception.class)
    public RedissonHandler redissonHandler() {
        /*return new RedissonHandlerImpl(redissonPath);*/
        Map<String,String> params = ImmutableMap.of("dataId", redissonPath, "group", "DEFAULT_GROUP");
        List<NameValuePair> pairs = params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        URI uri = new URIBuilder("http://" + nacosUrl + "/nacos/v1/cs/configs").addParameters(pairs).build();
        return new RedissonHandlerImpl(uri);
    }
}