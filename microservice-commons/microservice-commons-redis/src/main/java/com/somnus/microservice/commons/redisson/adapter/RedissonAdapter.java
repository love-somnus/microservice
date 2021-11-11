package com.somnus.microservice.commons.redisson.adapter;

import com.somnus.microservice.commons.redisson.handler.RedissonHandler;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.redisson.adapter
 * @title: RedissonAdapter
 * @description: TODO
 * @date 2019/6/14 11:23
 */
public interface RedissonAdapter {

    RedissonHandler getRedissonHandler();

}