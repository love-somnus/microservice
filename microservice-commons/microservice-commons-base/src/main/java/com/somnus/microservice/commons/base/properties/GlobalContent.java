package com.somnus.microservice.commons.base.properties;

import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.properties
 * @title: GlobalContent
 * @description: TODO
 * @date 2019/6/14 11:40
 */
public class GlobalContent {
    @Getter
    private String content;

    @SneakyThrows(IOException.class)
    public GlobalContent(String path, String encoding){
        @Cleanup
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        this.content = IOUtils.toString(inputStream, encoding);
    }

    @SneakyThrows(IOException.class)
    public GlobalContent(File file, String encoding){
        this.content = FileUtils.readFileToString(file, encoding);
    }

    @SneakyThrows(Exception.class)
    public GlobalContent(URI uri){
        this.content = Request.Get(uri).execute().returnContent().asString();
    }

}