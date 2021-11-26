package com.somnus.microservice.limit.redis.impl;

import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.impl
 * @title: RedisLimitExecutorImpl
 * @description: TODO
 * @date 2019/7/10 17:08
 */
@Slf4j
public class RedisLimitExecutorImpl implements LimitExecutor {

    @Autowired
    private RedisHandler redisHandler;

    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    @Override
    public boolean tryAccess(String name, String key, int limitPeriod, int limitCount) {
        if (StringUtils.isEmpty(name)) {
            throw new LimitException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LimitException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryAccess(compositeKey, limitPeriod, limitCount);
    }

    @Override
    public boolean tryAccess(String compositeKey, int limitPeriod, int limitCount) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        String luaScript = buildLuaScript();

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();

        Long count = redisTemplate.execute(RedisScript.of(luaScript, Long.class), Collections.singletonList(compositeKey), limitCount, limitPeriod);

        /* 执行 limitCount + 1后count 开始变为null，需要处理下空指针（不想用if，变通处理下）*/
        BigDecimal times = Optional.ofNullable(count) .map(BigDecimal::new).orElse(new BigDecimal(limitCount).add(BigDecimal.ONE));

        if (frequentLogPrint) {
            log.info("Access try count is {} for key={}", count, compositeKey);
        }

        return times.intValue() <= limitCount;
    }

    private String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then"); // 调用不超过最大值，则直接返回
        lua.append("\nreturn c;");
        lua.append("\nend");
        lua.append("\nc = redis.call('incr',KEYS[1])"); // 执行计算器自加
        lua.append("\nif tonumber(c) == 1 then");
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])"); // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nend");
        lua.append("\nreturn c;");

        return lua.toString();
    }
}