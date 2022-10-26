package com.somnus.microservice.limit.redis.impl;

import com.somnus.microservice.autoconfigure.proxy.util.Objects;
import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.redis.handler.RedisHandler;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redis.impl
 * @title: RedisLimitExecutorImpl
 * @description: TODO
 * @date 2019/7/10 17:08
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLimitExecutorImpl implements LimitExecutor {

    private final RedisHandler redisHandler;

    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    private static final DefaultRedisScript<List> SCRIPT;

    static {
        SCRIPT = new DefaultRedisScript<>();
        /*SCRIPT.setLocation(new ClassPathResource("fixed_window_limiter.lua"));*/
        SCRIPT.setLocation(new ClassPathResource("token_bucket_limter.lua"));
        SCRIPT.setResultType(List.class);
    }

    @Override
    public boolean tryAccess(String name, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (Objects.isEmpty(name)) {
            throw new LimitException("Name is null or empty");
        }

        if (Objects.isEmpty(key)) {
            throw new LimitException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryAccess(compositeKey, rate, rateInterval, rateIntervalUnit);
    }

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (Objects.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();

        /**
         * ARGV[1]：令牌桶最大长度
         * ARGV[2]：当前时间戳
         * ARGV[3]：重置桶内令牌的时间间隔(毫秒)
         * ARGV[4]：单位时间应该放入的令牌数
         * ARGV[5]：本次申请的令牌数量
         */
        List<Long> result = redisTemplate.execute(SCRIPT, Collections.singletonList(compositeKey),
                rate,
                Instant.now().toEpochMilli(),
                rateIntervalUnit.toMillis(rateInterval),
                rate,
                1);

        return result.get(0) >= result.get(1);
    }

    /*@Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        RedisTemplate<String, Object> redisTemplate = redisHandler.getRedisTemplate();

        //List<Long> result = redisTemplate.execute(RedisScript.of(luaScript, Long.class), Collections.singletonList(compositeKey), rate, rateIntervalUnit.toSeconds(rateInterval));
        List<Long> result = redisTemplate.execute(SCRIPT, Collections.singletonList(compositeKey), rate, rateIntervalUnit.toSeconds(rateInterval));

        if (frequentLogPrint) {
            log.info("Access try count is {} for key={}", result.get(1), compositeKey);
        }

        return Objects.requireNonNull(result).get(0) >= result.get(1);
    }*/

}