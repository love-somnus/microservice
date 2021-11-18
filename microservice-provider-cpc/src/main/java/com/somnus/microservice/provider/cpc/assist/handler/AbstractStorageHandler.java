package com.somnus.microservice.provider.cpc.assist.handler;

import cn.hutool.core.io.file.FileNameUtil;
import com.google.common.base.Joiner;
import com.somnus.microservice.commons.base.enums.ErrorCodeEnum;
import com.somnus.microservice.commons.base.exception.BusinessException;
import com.somnus.microservice.commons.core.support.Optionally;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.provider.cpc.assist.handler
 * @title: AbstractSmsHandler
 * @description: TODO
 * @date 2019/4/23 11:22
 */
@Slf4j
public abstract class AbstractStorageHandler<T> {

    private static final String DEFAULT_FOLDER = "resource";

    private static final String AES_KEY = "8d969eef6ecad3c";

    abstract public T client(String lang);

    /**
     * 异步上传文件流
     * @param objectName
     * @param file
     * @return
     */
    abstract public String asyncUpload(String lang, String objectName, MultipartFile file, T client);

    /**
     * 同步上传文件流
     * @param objectName
     * @param file
     * @return
     */
    abstract public String syncUpload(String lang, String objectName, MultipartFile file, T client);

    /**
     * 上传文件流(无需二次校验)
     * @param file
     * @return
     */
    @SneakyThrows
    public String upload(boolean isSync, String lang, MultipartFile file){

        String objectName = Joiner.on("/").skipNulls().join(
                lang,
                DEFAULT_FOLDER,
                DigestUtils.md5Hex(file.getInputStream()),
                Objects.requireNonNull(FileNameUtil.mainName(file.getOriginalFilename())).concat(".").concat(FileNameUtil.extName(file.getOriginalFilename()))
        );

        return isSync ? this.syncUpload(lang, objectName, file, client(lang)) : this.asyncUpload(lang, objectName, file, client(lang));
    }

    /**
     * 上传文件流(需要二次校验)
     * @param file
     * @return
     */
    @SneakyThrows
    public String upload(boolean isSync,String lang, String validateCode, MultipartFile file){

        if(! validateCode.equalsIgnoreCase("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92")){

            String md5 = DigestUtils.md5Hex(file.getOriginalFilename().concat(AES_KEY));

            log.info("file:[{}]--------> md5:[{}]--------> validateCode:[{}]", file.getOriginalFilename(), md5, validateCode);

            Optionally.ofNullable(validateCode).falseThrow(md5::equalsIgnoreCase, () -> new BusinessException(ErrorCodeEnum.EN10002));
        }

        return upload(isSync, lang, file);
    }

    /**
     *  备份文件
     * @param lang
     * @return
     */
    abstract public void backup(String lang);

    /**
     * 删除文件
     * @param lang
     * @param objectName
     */
    abstract public void delete(String lang, String objectName);
}