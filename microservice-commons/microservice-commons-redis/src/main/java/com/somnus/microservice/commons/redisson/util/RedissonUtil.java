package com.somnus.microservice.commons.redisson.util;

import com.somnus.microservice.commons.base.properties.GlobalContent;
import com.somnus.microservice.commons.redisson.constant.RedissonConstant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;

import java.io.IOException;
import java.net.URI;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redisson.util
 * @title: RedissonUtil
 * @description: TODO
 * @date 2019/6/14 11:28
 */
@Slf4j
public class RedissonUtil {

    /**
     * 创建Yaml格式的配置文件
     * @param yamlConfigPath
     * @return
     */
    public static Config createYamlFileConfig(String yamlConfigPath){
        log.info("Start to read {}...", yamlConfigPath);

        GlobalContent content = new GlobalContent(yamlConfigPath, RedissonConstant.ENCODING_UTF_8);

        return createYamlConfig(content.getContent());
    }

    /**
     * 创建Yaml格式的配置文件
     * @param uri
     * @return
     */
    public static Config createYamlFileConfig(URI uri){
        log.info("Start to read {}...", uri);

        GlobalContent content = new GlobalContent(uri);

        return createYamlConfig(content.getContent());
    }

    /**
     * 创建Json格式的配置文件
     * @param jsonConfigPath
     * @return
     */
    public static Config createJsonFileConfig(String jsonConfigPath){
        log.info("Start to read {}...", jsonConfigPath);

        GlobalContent content = new GlobalContent(jsonConfigPath, RedissonConstant.ENCODING_UTF_8);

        return createJsonConfig(content.getContent());
    }

    /**
     * 创建Yaml格式的配置文件
     * @param yamlConfigContent
     * @return
     */
    @SneakyThrows(IOException.class)
    public static Config createYamlConfig(String yamlConfigContent){
        return Config.fromYAML(yamlConfigContent);
    }

    /**
     * 创建Json格式的配置文件
     * @param jsonConfigContent
     * @return
     */
    @SneakyThrows(IOException.class)
    public static Config createJsonConfig(String jsonConfigContent){
        return Config.fromJSON(jsonConfigContent);
    }
}