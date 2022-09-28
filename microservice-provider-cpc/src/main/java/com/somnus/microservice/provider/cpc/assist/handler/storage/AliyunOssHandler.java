package com.somnus.microservice.provider.cpc.assist.handler.storage;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import com.somnus.microservice.commons.base.enums.HandlerType;
import com.somnus.microservice.provider.cpc.assist.handler.AbstractStorageHandler;
import com.somnus.microservice.provider.cpc.listener.OssProgressListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.handler.storage
 * @title: AliyunOssHandler
 * @description: TODO
 * @date 2019/12/23 17:37
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
@HandlerType(values = {"zh-tw"})
public class AliyunOssHandler extends AbstractStorageHandler<OSSClient>{

    private final Environment env;

    private final AsyncTaskExecutor asyncExecutor;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public OSSClient client(String lang){

        CredentialsProvider credentials = new DefaultCredentialProvider(
                Objects.requireNonNull(env.getProperty(MessageFormat.format("wt.storage.{0}.accessKey", lang))),
                Objects.requireNonNull(env.getProperty(MessageFormat.format("wt.storage.{0}.secretKey", lang)))
        );

        // 创建OSSClient实例。
        return new OSSClient(env.getProperty(MessageFormat.format("wt.storage.{0}.endPoint", lang)), credentials, (ClientConfiguration)null);
    }

    @Override
    @SneakyThrows
    public String asyncUpload(String lang, String objectName, MultipartFile file, OSSClient client){

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        asyncExecutor.execute(() -> {
            // 带进度条的上传
            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                client.putObject(new PutObjectRequest(bucket, objectName, file.getInputStream(), metadata).<PutObjectRequest>withProgressListener(new OssProgressListener(objectName, file.getInputStream().available(), stringRedisTemplate)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 关闭OSSClient。
            client.shutdown();
        });

        return MessageFormat.format("{0}/{1}", env.getProperty(MessageFormat.format("wt.storage.{0}.cdnUrl", lang)), objectName);
    }

    @Override
    @SneakyThrows
    public String syncUpload(String lang, String objectName, MultipartFile file, OSSClient client) {

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(file.getContentType());

        // 上传文件流。
        client.putObject(bucket, objectName, file.getInputStream(), metadata);

        // 关闭OSSClient。
        client.shutdown();

        return MessageFormat.format("{0}/{1}", env.getProperty(MessageFormat.format("wt.storage.{0}.cdnUrl", lang)), objectName);
    }


    @Override
    public void backup(String lang) {

        String sourceBucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        String backupBucket = env.getProperty(MessageFormat.format("wt.storage.{0}.backup-bucket", lang));

        String backupCdnUrl = env.getProperty(MessageFormat.format("wt.storage.{0}.backup-cdnUrl", lang));

        String nextMarker = null;

        ObjectListing objectListing;

        OSSClient client = client(lang);

        do {

            objectListing = client.listObjects(new ListObjectsRequest(sourceBucket).withMarker(nextMarker).withMaxKeys(1000));

            nextMarker = objectListing.getNextMarker();

            objectListing.getObjectSummaries().parallelStream().forEach(item ->{

                OSSClient innerClient = client(lang);

                // 创建CopyObjectRequest对象。
                CopyObjectRequest copyObjectRequest = new CopyObjectRequest(sourceBucket, item.getKey(), backupBucket, item.getKey());
                // 复制文件。
                try {
                    innerClient.copyObject(copyObjectRequest);
                    log.info("阿里云桶中的资源[{}/{}]备份成功♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪", backupCdnUrl, item.getKey());
                } finally {
                    // 无聊发生异常与否，都关闭OSSClient。
                    innerClient.shutdown();
                }
            });

        } while (objectListing.isTruncated());

        client.shutdown();

        log.info("阿里云桶中的资源备份完毕，♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪共{}条", objectListing.getObjectSummaries().size());
    }

    @Override
    public void delete(String lang, String objectName) {

        OSSClient client = client(lang);

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        client.deleteObject(bucket, objectName);

        // 关闭OSSClient。
        client.shutdown();

        log.info("成功删除bucket:[{}]中的oss文件[{}]", bucket , objectName);
    }
}