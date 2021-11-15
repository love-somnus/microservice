package com.somnus.microservice.commons.core.https;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils.https
 * @title: HttpClientManager
 * @description: TODO
 * @date 2019/3/15 16:54
 */
@Slf4j
public class HttpClientManager {

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_PER_ROUTE = 400;
    public final static int AFTER_INACTIVITY = 1000;

    private static PoolingHttpClientConnectionManager defaultSSLConnManager;

    private static final Map<KeyStoreMaterial, PoolingHttpClientConnectionManager> sslConnManager = new HashMap<KeyStoreMaterial, PoolingHttpClientConnectionManager>();

    private static final Object sslLock = new Object();

    static {
        try {
            SSLContext sslcontext = SSLContexts.custom()
                    //忽略掉对服务器端证书的校验
                    .loadTrustMaterial(new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    })
                    .build();
            defaultSSLConnManager = getSSLContextConnManager(sslcontext);
            log.info("默认SSL连接,不携带客户端证书初始化完毕");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化建立连接异常:{}", e.getMessage(), e);
        }
    }

    /**
     * getSSLContextConnManager: 根据SSLContext初始化一个连接管理器 <br/>
     *
     * @param sslcontext
     * @return
     * @since JDK 1.7
     */
    private static PoolingHttpClientConnectionManager getSSLContextConnManager(SSLContext sslcontext) {
        // 服务器验证策略使用DNS域名解析
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        // Create a registry of custom connection socket factories for supported
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        connManager.setValidateAfterInactivity(AFTER_INACTIVITY);

        return connManager;
    }

    /**
     * getSSLConnManager:根据keyStore获取一个连接池管理器 <br/>
     * @param path
     * @param password
     * @return
     */
    private static PoolingHttpClientConnectionManager getSSLConnManager(String path, String password) {
        KeyStoreMaterial keyStore = KeyStoreUtil.getKeyStore(path, password);
        if (keyStore == null) {
            log.info("keyStore is null,will get default SSLContextConnManager");
            return defaultSSLConnManager;
        }
        PoolingHttpClientConnectionManager cm = sslConnManager.get(keyStore);
        if (cm == null) {
            synchronized (sslLock) {
                cm = sslConnManager.get(keyStore);
                if (cm == null) {
                    try {
                        log.info("trying to obtain SSLContextConnManager by certificate path:[{}],password:[{}] ", path, password);
                        SSLContext sslcontext = SSLContexts.custom()
                                .loadKeyMaterial(keyStore.getKeyStore(), keyStore.getPassword().toCharArray())
                                .build();
                        cm = getSSLContextConnManager(sslcontext);
                        sslConnManager.put(keyStore, cm);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("path:{},password:{}建立连接异常:{}", path, password, e.getMessage(), e);
                    }
                }
            }
        }
        return cm;
    }

    /**
     * getSSLHttpClient:获取默认的SSL,不携带客户端证书<br/>
     *
     * @return
     * @since JDK 1.7
     */
    public static CloseableHttpClient getSSLHttpClient(){
        return getSSLHttpClient("", "");
    }

    /**
     * getSSLHttpClient:client/server certification<br/>
     *
     * @param path
     * @param password
     * @return
     * @since JDK 1.7
     */
    public static CloseableHttpClient getSSLHttpClient(String path, String password){

        PoolingHttpClientConnectionManager connManager = getSSLConnManager(path, password);

        return HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
    }


}
