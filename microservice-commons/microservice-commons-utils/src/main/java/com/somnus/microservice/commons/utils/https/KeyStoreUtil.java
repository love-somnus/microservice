package com.somnus.microservice.commons.utils.https;

import com.somnus.microservice.commons.utils.PublicUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils.https
 * @title: KeyStoreUtil
 * @description: TODO
 * @date 2019/3/15 16:58
 */
public class KeyStoreUtil {

    private static Map<String, KeyStoreMaterial> keystores = new HashMap<String, KeyStoreMaterial>();

    private static final Object lock = new Object();

    /**
     * getKeyStore:获取证书<br/>
     *
     * @param path
     * @param password
     * @return
     * @since JDK 1.7
     */
    public static KeyStoreMaterial getKeyStore(String path, String password) {
        if (PublicUtil.isEmpty(path) || PublicUtil.isEmpty(password)) {
            return null;
        }
        String key = formatKey(new String[]{path, password});
        KeyStoreMaterial result = keystores.get(key);
        if (result == null) {
            synchronized (lock) {
                result = keystores.get(key);
                if (result == null) {
                    try {
                        KeyStore keyStore = loadItemTokeyStore(path, password);
                        result = new KeyStoreMaterial(password, keyStore);
                        keystores.put(key, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * formatKey:生成key. <br/>
     *
     * @param strs
     * @return
     * @since JDK 1.7
     */
    private static String formatKey(String[] strs) {
        StringBuffer sb = new StringBuffer();
        for (String str : strs) {
            sb.append(str).append("#");
        }
        return sb.toString();
    }

    /**
     * loadItemTokeyStore:获取证书 <br/>
     *
     * @param path
     * @param password
     * @return
     * @since JDK 1.7
     */
    @SneakyThrows
    private static KeyStore loadItemTokeyStore(String path, String password){
        KeyStore keyStore = null;
        @Cleanup
        InputStream is = null;
        if (path.startsWith("classpath:")) {
            path = path.substring("classpath:".length());
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        } else if (path.startsWith("file:")) {
            path = path.substring("file:".length());
            is = new BufferedInputStream(new FileInputStream(path));
        }
        if (is != null) {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(is, password.toCharArray());
        }

        return keyStore;
    }

}