package com.somnus.microservice.commons.base.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;
/**
 * @author kevin.liu
 * @title: JwtTokenUtil
 * @projectName commons
 * @description: TODO
 * @date 2021/11/15 15:46
 */
@Slf4j
public class JwtTokenUtil {

    private final static Clock CLOCK = DefaultClock.INSTANCE;


    /* 寻找证书文件 */
    private static final InputStream INPUT_STREAM = Thread.currentThread().getContextClassLoader().getResourceAsStream("mirror-privateKey.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static { // 将证书文件里边的私钥公钥拿出来
        try {
            /* java key store 固定常量 */
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(INPUT_STREAM, "3d-mirror".toCharArray());
            /* jwt 为 命令生成整数文件时的别名*/
            privateKey = (PrivateKey) keyStore.getKey("mirror-privateKey", "3d-mirror".toCharArray());
            publicKey = keyStore.getCertificate("mirror-privateKey").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @date 2021/3/9 17:36
     * @description 生成jwt token
     */
    public static String generateToken(Map<String, Object> claims, String subject, long expiration, TemporalUnit unit) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        /* 生成签名密钥 */
        final Date createdDate = CLOCK.now();
        final LocalDateTime expirationDate = LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()).plus(expiration, unit);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(privateKey)
                .compact();
    }

    /**
     * 解密Jwt内容
     *
     * @param jwt jwt
     * @return Claims
     */
    public static Claims parseJwtRsa256(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}