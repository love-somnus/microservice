package com.somnus.microservice.commons.base.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    private static final Clock CLOCK = DefaultClock.INSTANCE;

    private static final String secretKey = "V1beFinb07YUJuAjdBevbvCqv9FNqyw4KhM5bMKxCyU=";

    private static final Key key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

    /**
     * @date 2021/3/9 17:36
     * @description 生成jwt token
     */
    public static String generateToken(Map<String, Object> claims, String subject, long expiration, TemporalUnit unit) {
        /* 生成签名密钥 */
        final Date createdDate = CLOCK.now();
        final LocalDateTime expirationDate = LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()).plus(expiration, unit);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
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
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) {
        String jwt = JwtTokenUtil.generateToken(ImmutableMap.of(
                "userId", 1,
                "username", "admin",
                "roles", Ints.asList(1, 2, 3)), "jwt", 8L, ChronoUnit.HOURS);
        System.out.println(jwt);
        System.out.println(JwtTokenUtil.parseJwtRsa256(jwt));
    }
}