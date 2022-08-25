package com.somnus.microservice.commons.base.utils;

import com.somnus.microservice.commons.base.constant.GlobalConstant;
import com.somnus.microservice.commons.base.dto.LoginAuthDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: RequestUtil
 * @description: TODO
 * @date 2019/4/18 13:50
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

    private static String tmpPath = null;

    private static byte[] vectorIndexBuff;

    private static byte[] contentBuff;

    static {
        try {
            Path path = Files.createTempFile("ip2region", ".xdb");
            InputStream is = new URL("https://web-static.baochuangames.com/nos/geo/ip2region.xdb").openStream();
            Files.write(path, IOUtils.toByteArray(is));
            tmpPath = path.toString();
            log.info("临时文件目录:{}", tmpPath);
            // 缓存 VectorIndex 索引
            vectorIndexBuff = Searcher.loadVectorIndexFromFile(tmpPath);
            // 缓存整个 xdb 数据
            contentBuff = Searcher.loadContentFromFile(tmpPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets request.
     *
     * @return the request
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获得用户远程地址
     * @return
     */
    public static String getRemoteAddr(){
        return getRemoteAddr(getRequest());
    }

    /**
     * 获得用户User-Agent
     * @return
     */
    public static String getUserAgent(){
        return getRequest().getHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * 获得用户header值
     * @return
     */
    @SneakyThrows
    public static String getHeader(String key){

        String value = getRequest().getHeader(key);

        return java.net.URLDecoder.decode(value, "UTF-8");
    }

    /**
     * 获得Query查询串
     * @return
     */
    public static String getQueryString(){
        return getRequest().getQueryString();
    }

    /**
     * 获得用户远程地址
     *
     * @param request the request
     *
     * @return the string
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader(GlobalConstant.X_CLIENT_IP);
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.X_REAL_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.HTTP_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader(GlobalConstant.HTTP_X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (GlobalConstant.LOCALHOST_IP.equals(ipAddress) || GlobalConstant.LOCALHOST_IP_16.equals(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("获取IP地址, 出现异常={}", e.getMessage(), e);
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
            log.info("获取IP地址 ipAddress={}", ipAddress);
        }
        // 对于通过多个代理的情况, 第一个IP为客户端真实IP,多个IP按照','分割 //"***.***.***.***".length() = 15
        if (ipAddress != null && ipAddress.length() > GlobalConstant.MAX_IP_LENGTH) {
            if (ipAddress.indexOf(GlobalConstant.Symbol.COMMA) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(GlobalConstant.Symbol.COMMA));
            }
        }
        return ipAddress;
    }

    /**
     * Gets login auth dto.
     *
     * @return the login auth dto
     */
    public static LoginAuthDto getLoginUser() {
        HttpServletRequest request = getRequest();
        LoginAuthDto loginAuthDto;
        try{
            loginAuthDto = new LoginAuthDto();
        } catch (Exception e){
            log.error("未能正确获取登陆用户信息" , e);
            loginAuthDto = new LoginAuthDto(0L, "admin", "非正常用户");
        }
        return loginAuthDto;
    }

    /**
     * 每个线程需要单独创建一个独立的 Searcher 对象，但是都共享全局的制度 vIndex 缓存。
     * @param ip        ip
     * @return
     */
    @SneakyThrows
    public static List<String> getIpAddress(String ip) {

        // 使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher = Searcher.newWithVectorIndex(tmpPath, vectorIndexBuff);

        long sTime = System.nanoTime();

        String region = searcher.search(ip);

        long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);

        log.info("{region: {}, ioCount: {}, took: {} μs}\n", region, searcher.getIOCount(), cost);

        return Arrays.stream(region.split("\\|")).filter(s -> !"0".equals(s)).collect(Collectors.toList());
    }

    /**
     * 并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，
     * 也就是你可以把这个 searcher 对象做成全局对象去跨线程访问
     * @param ip   ip
     * @return
     */
    @SneakyThrows
    public static List<String> getIpAddress2(String ip) {

        // 使用上述的 contentBuff 创建一个完全基于内存的查询对象。
        Searcher searcher = Searcher.newWithBuffer(contentBuff);

        long sTime = System.nanoTime();

        String region = searcher.search(ip);

        long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);

        log.info("{region: {}, ioCount: {}, took: {} μs}\n", region, searcher.getIOCount(), cost);

        return Arrays.stream(region.split("\\|")).filter(s -> !"0".equals(s)).collect(Collectors.toList());
    }

    @SneakyThrows
    public static String getCountry(String ip){
        if(ip.startsWith("192.168.") || ip.startsWith("172.")){
            return "局域网";
        }
        return getIpAddress(ip).get(0);
    }

    @SneakyThrows
    public static String getProvince(String ip) {
        if(ip.startsWith("192.168.") || ip.startsWith("172.")){
            return "局域网";
        }
        return getIpAddress(ip).get(1);
    }

    @SneakyThrows
    public static String getCity(String ip) {
        if(ip.startsWith("192.168.") || ip.startsWith("172.")){
            return "局域网";
        }
        return getIpAddress(ip).get(2);
    }

}