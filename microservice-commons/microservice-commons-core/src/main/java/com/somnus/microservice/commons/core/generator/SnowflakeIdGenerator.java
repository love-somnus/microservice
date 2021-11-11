package com.somnus.microservice.commons.core.generator;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.generator
 * @title: SnowflakeIdGenerator
 * @description: TODO
 * @date 2019/4/24 17:29
 */
@Slf4j
public class SnowflakeIdGenerator implements IdGenerator{

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    private static final long DEFAULT_START_TIMESTAMP = 1483200000000L; // 2017-01-01 00:00:00:000

    private final Map<String, SnowflakeIdProcessor> idGeneratorMap = new ConcurrentHashMap<>();

    @Override
    public String nextUniqueId(long dataCenterId, long machineId){
        return nextUniqueId(DEFAULT_START_TIMESTAMP, dataCenterId, machineId);
    }

    @Override
    public String nextUniqueId(String startTimestamp, long dataCenterId, long machineId){
        return nextUniqueId(LocalDateTime.parse(startTimestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), dataCenterId, machineId);
    }

    @Override
    public String nextUniqueId(long startTimestamp, long dataCenterId, long machineId){
        String nextUniqueId = getIdProcessor(startTimestamp, dataCenterId, machineId).nextId();
        log.info("Next unique id is {} for startTimestamp={}, dataCenterId={}, machineId={}", nextUniqueId, startTimestamp, dataCenterId, machineId);
        return nextUniqueId;
    }

    @Override
    public String[] nextUniqueIds(long dataCenterId, long machineId, int count) {
        return nextUniqueIds(DEFAULT_START_TIMESTAMP, dataCenterId, machineId, count);
    }

    @Override
    public String[] nextUniqueIds(String startTimestamp, long dataCenterId, long machineId, int count){
        return nextUniqueIds(LocalDateTime.parse(startTimestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), dataCenterId, machineId, count);
    }

    @Override
    public String[] nextUniqueIds(long startTimestamp, long dataCenterId, long machineId, int count) {
        String[] nextUniqueIds = getIdProcessor(startTimestamp, dataCenterId, machineId).nextIds(count);
        log.info("Next unique ids is {} for startTimestamp={}, dataCenterId={}, machineId={}, count={}", Joiner.on(",").skipNulls().join(nextUniqueIds), startTimestamp, dataCenterId, machineId, count);
        return nextUniqueIds;
    }

    private SnowflakeIdProcessor getIdProcessor(long startTimestamp, long dataCenterId, long machineId) {
        String key = dataCenterId + "-" + machineId;

        SnowflakeIdProcessor processor = idGeneratorMap.get(key);
        if (processor == null) {
            SnowflakeIdProcessor newProcessor = new SnowflakeIdProcessor(startTimestamp, dataCenterId, machineId);
            processor = idGeneratorMap.putIfAbsent(key, newProcessor);
            if (processor == null) {
                processor = newProcessor;
            }
        }

        return processor;
    }

}
