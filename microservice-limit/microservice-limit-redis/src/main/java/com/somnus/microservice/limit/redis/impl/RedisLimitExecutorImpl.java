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
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

        List<String> keys = new ArrayList<>();
        keys.add(compositeKey);

        String luaScript = buildLuaScript();

        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();
        Number count = Optional.ofNullable(redisTemplate.execute(redisScript, keys, limitCount, limitPeriod)).orElse(new BigDecimal(limitCount).add(BigDecimal.ONE));

        if (frequentLogPrint) {
            log.info("Access try count is {} for key={}", count, compositeKey);
        }

        return count.intValue() <= limitCount;
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