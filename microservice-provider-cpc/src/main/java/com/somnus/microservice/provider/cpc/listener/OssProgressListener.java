package com.somnus.microservice.provider.cpc.listener;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.google.common.base.Stopwatch;
import com.somnus.microservice.autoconfigure.proxy.util.Pair;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @title: OssProgressListener
 * @description: TODO
 * @date 2019/9/2 10:46
 */
@Slf4j
@NoArgsConstructor
public class OssProgressListener implements ProgressListener {

    private long bytesWritten = 0;
    private int totalBytes = -1;
    private boolean succeed = false;

    private String objectName;

    private static ThreadLocal<Pair<String, Integer>> threadLocal;

    private StringRedisTemplate stringRedisTemplate;

    private Stopwatch stopwatch;

    public OssProgressListener(String objectName, int totalBytes, StringRedisTemplate stringRedisTemplate){
        this.objectName = objectName;
        this.totalBytes = totalBytes;
        this.stringRedisTemplate = stringRedisTemplate;
        threadLocal = ThreadLocal.withInitial(() -> new Pair(objectName , -1));
        stopwatch = Stopwatch.createStarted();
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.info("Start to upload [{}]......", objectName);
                break;

            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                if(! threadLocal.get().getValue().equals(percent)){
                    log.info(objectName + " have been written " + bytes + " bytes at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    stringRedisTemplate.opsForValue().set(objectName, percent + "%", 600, TimeUnit.SECONDS);
                }
                threadLocal.set(new Pair<>(objectName, percent));
                break;

            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                threadLocal.remove();
                stopwatch.stop();
                log.info("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total，消耗时间[{}]", stopwatch.toString());
                break;

            case TRANSFER_FAILED_EVENT:
                log.info("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;

            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}
