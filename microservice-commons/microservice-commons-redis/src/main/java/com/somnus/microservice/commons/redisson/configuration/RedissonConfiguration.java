package com.somnus.microservice.commons.redisson.configuration;

import com.google.common.collect.ImmutableMap;
import com.somnus.microservice.commons.redisson.adapter.RedissonAdapter;
import com.somnus.microservice.commons.redisson.constant.RedissonConstant;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.commons.redisson.handler.RedissonHandlerImpl;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @description: TODO
 * @date 2019/7/5 16:42
 */
@Configuration
public class RedissonConfiguration {

    @Value("${" + RedissonConstant.NACOS_URL + "}")
    private String nacosUrl;

    @Value("${" + RedissonConstant.PATH + ":" + RedissonConstant.DEFAULT_PATH + "}")
    private String redissonPath;

    @Autowired(required = false)
    private RedissonAdapter redissonAdapter;

    @Bean
    @SneakyThrows(Exception.class)
    public RedissonHandler redissonHandler() {
        if (redissonAdapter != null) {
            return redissonAdapter.getRedissonHandler();
        }
        /*return new RedissonHandlerImpl(redissonPath);*/
        Map<String,String> params = ImmutableMap.of("dataId", redissonPath, "group", "DEFAULT_GROUP");
        List<NameValuePair> pairs = params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        URI uri = new URIBuilder("http://" + nacosUrl + "/nacos/v1/cs/configs").addParameters(pairs).build();
        return new RedissonHandlerImpl(uri);
    }
}