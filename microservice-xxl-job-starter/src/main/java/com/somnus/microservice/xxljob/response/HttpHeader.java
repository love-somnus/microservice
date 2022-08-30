package com.somnus.microservice.xxljob.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author kevin.liu
 * @title: HttpHeader
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 17:42
 */
@Data
@RequiredArgsConstructor
public class HttpHeader implements Serializable {

    private final String headerName;

    private final String headerValue;

}