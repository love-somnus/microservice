package com.somnus.microservice.provider.cpc.assist.adapter;

import com.somnus.microservice.provider.cpc.assist.handler.*;
import com.somnus.microservice.commons.core.support.SpringContextHolder;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.adapter
 * @title: HandlerContext
 * @description: TODO
 * @date 2019/12/23 16:09
 */
public class StorageHandlerAdapter {

    private Map<String, Class<?>> handlerMap;

    public StorageHandlerAdapter(Map<String, Class<?>> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public AbstractStorageHandler getHandler(String lang) {

        Class<?> clazz = handlerMap.get(lang);

        Optional.ofNullable(clazz).orElseThrow(() -> new IllegalArgumentException("not found AbstractStorageHandler for game: " + lang));

        return (AbstractStorageHandler)SpringContextHolder.getBean(clazz);
    }

}