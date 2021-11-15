package com.somnus.microservice.commons.core.https;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.KeyStore;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils.https
 * @title: KeyStoreMaterial
 * @description: TODO
 * @date 2019/3/15 16:56
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class KeyStoreMaterial {

    /**  密码 */
    private String password;

    /** keyStore */
    private KeyStore keyStore;

}