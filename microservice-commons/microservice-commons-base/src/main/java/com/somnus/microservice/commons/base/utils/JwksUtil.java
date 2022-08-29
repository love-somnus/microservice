package com.somnus.microservice.commons.base.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;

/**
 * @author kevin
 * @title: Jwks
 * @description: keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
 * @date 2021/11/16 16:16
 */
@UtilityClass
public class JwksUtil {

    private static final Clock CLOCK = DefaultClock.INSTANCE;
    private static final RSAPrivateKey privateKey;
    private static final RSAPublicKey publicKey ;

    static {
        try {
            /*KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();*/

            //从classpath下获取RSA秘钥对
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "passw0rd".toCharArray());
            KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt", "passw0rd".toCharArray());
            //获取RSA公钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
            //获取RSA私钥
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static RSAKey generateRsa() {
        return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    }

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

    public static void main(String[] args) {

        System.out.println(new JWKSet(generateRsa()).toJSONObject());

        String jwt = JwksUtil.generateToken(ImmutableMap.of(
                "userId", 1,
                "username", "admin",
                "roles", Ints.asList(1, 2, 3)), "jwt", 8L, ChronoUnit.HOURS);

        System.out.println(jwt);

        System.out.println(JwksUtil.parseJwtRsa256(jwt));
    }
}
