package com.somnus.microservice.provider.cpc.assist.handler.storage;

import com.google.common.base.Stopwatch;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.*;
import com.netease.cloud.services.nos.transfer.TransferManager;
import com.netease.cloud.services.nos.transfer.Upload;
import com.netease.cloud.services.nos.transfer.model.UploadResult;
import com.somnus.microservice.commons.base.enums.HandlerType;
import com.somnus.microservice.provider.cpc.assist.handler.AbstractStorageHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.handler.storage
 * @title: NeteaseNosHandler
 * @description: TODO
 * @date 2019/12/23 17:37
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
@HandlerType(values = {"zh-cn", "zh-hk", "zh-sh"})
public class NeteaseNosHandler extends AbstractStorageHandler<NosClient> {

    private final Environment env;

    private final AsyncTaskExecutor asyncExecutor;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public NosClient client(String lang){

        Credentials credentials = new BasicCredentials(
                env.getProperty(MessageFormat.format("wt.storage.{0}.accessKey", lang)),
                env.getProperty(MessageFormat.format("wt.storage.{0}.secretKey", lang))
        );

        // 创建NosClient实例。
        NosClient client = new NosClient(credentials);

        client.setEndpoint(env.getProperty(MessageFormat.format("wt.storage.{0}.endPoint", lang)));

        return client;
    }

    @Override
    @SneakyThrows
    public String asyncUpload(String lang, String objectName, MultipartFile file, NosClient client){

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        String contentType = file.getContentType();

        InputStream is = file.getInputStream();

        asyncExecutor.execute(new NeteaseNosHandler.UploadRunnable(bucket, objectName, client, contentType, is, stringRedisTemplate));

        return MessageFormat.format("{0}/{1}", env.getProperty(MessageFormat.format("wt.storage.{0}.cdnUrl", lang)), objectName);
    }

    @Override
    @SneakyThrows
    public String syncUpload(String lang, String objectName, MultipartFile file, NosClient client) {

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        //通过nosClient对象来初始化TransferManager
        TransferManager transferManager = new TransferManager(client);

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getInputStream().available());
        metadata.setContentType(file.getContentType());

        Upload upload = transferManager.upload(bucket, objectName, file.getInputStream(), metadata);

        UploadResult result = upload.waitForUploadResult();

        transferManager.shutdownNow();

        // 关闭NosClient
        client.shutdown();

        log.info("成功同步上传bucket:[{}]中的nos文件[{}]", bucket , objectName);

        return MessageFormat.format("{0}/{1}", env.getProperty(MessageFormat.format("wt.storage.{0}.cdnUrl", lang)), result.getKey());
    }

    public static class UploadRunnable implements Runnable{

        private String bucket;
        private String objectName;
        private String contentType;
        private NosClient client;
        private InputStream is;
        private StringRedisTemplate stringRedisTemplate;

        public UploadRunnable(String bucket, String objectName, NosClient client, String contentType, InputStream is, StringRedisTemplate stringRedisTemplate) {
            this.bucket = bucket;
            this.objectName = objectName;
            this.client = client;
            this.contentType = contentType;
            this.is = is;
            this.stringRedisTemplate = stringRedisTemplate;
        }

        @Override
        @SneakyThrows
        public void run() {
            Stopwatch stopwatch = Stopwatch.createStarted();

            /** ①初始化分块上传*/
            InitiateMultipartUploadRequest  initRequest = new InitiateMultipartUploadRequest(bucket, objectName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            initRequest.setObjectMetadata(metadata);
            InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(initRequest);
            String uploadId = initResult.getUploadId();

            int partNumber = 1;
            int partSize = 0;
            int bytesWritten = 0;
            int totalBytes = is.available();
            int buffSize = 10*1024*1024;
            byte[] buffer = new byte[10*1024*1024];
            log.info("Start to upload......");

            /** ②进行分块上传*/
            while ((partSize = is.read(buffer, 0, buffSize)) != -1 ){
                InputStream partStream = new ByteArrayInputStream(buffer);
                UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucket).withUploadId(uploadId).withInputStream(partStream).withKey(objectName).withPartSize(partSize).withPartNumber(partNumber);
                client.uploadPart(uploadRequest);

                partNumber++;
                //计算进度条
                bytesWritten += partSize;
                int percent = (int)(bytesWritten * 100.0 / totalBytes);
                log.info(objectName + " have been written " + partSize + " bytes at this time, upload progress: " + percent + "%(" + bytesWritten + "/" + totalBytes + ")");
                stringRedisTemplate.opsForValue().set(objectName, percent + "%", 600, TimeUnit.SECONDS);
            }

            /** ③列出所有分块*/
            List<PartETag> partETags = new ArrayList<>();
            int nextMarker = 0;
            while (true) {
                ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, objectName, uploadId);
                listPartsRequest.setPartNumberMarker(nextMarker);
                PartListing partList = client.listParts(listPartsRequest);
                for (PartSummary ps : partList.getParts()) {
                    nextMarker++;
                    partETags.add(new PartETag(ps.getPartNumber(), ps.getETag()));
                }

                if (!partList.isTruncated()) {
                    break;
                }
            }

            /** ④完成分块上传*/
            CompleteMultipartUploadRequest completeRequest =  new CompleteMultipartUploadRequest(bucket, objectName, uploadId, partETags);
            CompleteMultipartUploadResult completeResult = client.completeMultipartUpload(completeRequest);
            stopwatch.stop();

            // 关闭NosClient
            client.shutdown();

            log.info("成功异步上传bucket:[{}]中的nos文件[{}]，消耗时间[{}]", bucket , initResult.getKey(), stopwatch.toString());
        }
    }

    @Override
    public void backup(String lang) {

        NosClient client = client(lang);

        String sourceBucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        String backupBucket = env.getProperty(MessageFormat.format("wt.storage.{0}.backup-bucket", lang));

        String backupCdnUrl = env.getProperty(MessageFormat.format("wt.storage.{0}.backup-cdnUrl", lang));

        List<NOSObjectSummary> listResult = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(sourceBucket);
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing listObjects = client.listObjects(listObjectsRequest);
        do {
            listResult.addAll(listObjects.getObjectSummaries());
            if (listObjects.isTruncated()) {
                ListObjectsRequest request = new ListObjectsRequest();
                request.setBucketName(listObjectsRequest.getBucketName());
                request.setMarker(listObjects.getNextMarker());
                listObjects =  client.listObjects(request);
            } else {
                break;
            }
        } while (listObjects != null);

        listResult.parallelStream().forEach(item -> {
            NosClient innerClient = client(lang);
            try {
                innerClient.copyObject(sourceBucket, item.getKey(), backupBucket, item.getKey());
                log.info("网易云桶中的资源[{}/{}]备份成功♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪", backupCdnUrl, item.getKey());
            } finally {
                // 无聊发生异常与否，都关闭OSSClient。
                innerClient.shutdown();
            }
        });

        client.shutdown();

        log.info("网易云桶中的资源备份完毕，♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪♪共{}条", listResult.size());
    }

    @Override
    public void delete(String lang, String objectName) {

        NosClient client = client(lang);

        String bucket = env.getProperty(MessageFormat.format("wt.storage.{0}.bucket", lang));

        client.deleteObject(bucket, objectName);

        // 关闭NosClient
        client.shutdown();

        log.info("成功删除bucket:[{}]中的nos文件[{}]", bucket , objectName);
    }
}