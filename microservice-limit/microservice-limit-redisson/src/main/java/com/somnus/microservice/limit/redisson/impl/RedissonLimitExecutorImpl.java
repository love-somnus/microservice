package com.somnus.microservice.limit.redisson.impl;

import com.somnus.microservice.autoconfigure.selector.KeyUtil;
import com.somnus.microservice.commons.redisson.handler.RedissonHandler;
import com.somnus.microservice.limit.LimitExecutor;
import com.somnus.microservice.limit.constant.LimitConstant;
import com.somnus.microservice.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.limit.redisson.impl
 * @title: RedissonLimitExecutorImpl
 * @description: TODO
 * @date 2019/7/10 17:08
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLimitExecutorImpl implements LimitExecutor {

    private final RedissonHandler redissonHandler;

    @Value("${" + LimitConstant.PREFIX + "}")
    private String prefix;

    @Value("${" + LimitConstant.FREQUENT_LOG_PRINT + ":false}")
    private Boolean frequentLogPrint;

    @Override
    public boolean tryAccess(String name, String key, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StringUtils.isEmpty(name)) {
            throw new LimitException("Name is null or empty");
        }

        if (StringUtils.isEmpty(key)) {
            throw new LimitException("Key is null or empty");
        }

        String compositeKey = KeyUtil.getCompositeKey(prefix, name, key);

        return tryAccess(compositeKey, rate, rateInterval, rateIntervalUnit);
    }

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StringUtils.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        RRateLimiter rateLimiter = redissonHandler.getRedisson().getRateLimiter(compositeKey);

        if (frequentLogPrint) {
            /*log.info("Access try count is {} for key={}", count, compositeKey);*/
        }
        switch (rateIntervalUnit){
            case MILLISECONDS:
                /* 设置速率，rateInterval 毫秒中产生 rate 个令牌 */
                rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.MILLISECONDS);
                break;
            case SECONDS:
                /* 设置速率，rateInterval 秒中产生 rate 个令牌 */
                rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.SECONDS);
                break;
            case MINUTES:
                /* 设置速率，rateInterval 分钟中产生 rate 个令牌 */
                rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.MINUTES);
                break;
            case HOURS:
                /* 设置速率，rateInterval 小时中产生 rate 个令牌 */
                rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.HOURS);
                break;
            default:
                log.error("unsupport TimeUnit {}", rateIntervalUnit);
                break;
        }

        /* 试图获取1个令牌，获取到返回true */
        return rateLimiter.tryAcquire(1);
    }

}