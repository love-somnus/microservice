package com.somnus.microservice.commons.base.utils;

import com.somnus.microservice.commons.base.constant.GlobalConstant;
import com.somnus.microservice.commons.base.holder.ReactiveRequestContextHolder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.utils
 * @title: ReactiveRequestUtil
 * @description: TODO
 * @date 2019/4/18 13:50
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactiveRequestUtil {


    /**
     * Gets request.
     *
     * @return the request
     */
    public static ServerHttpRequest getRequest() {
        return ReactiveRequestContextHolder.get();
    }

    /**
     * Gets header.
     *
     * @return the getHttpHeader
     */
    public static String getHttpHeader(String header) {
        return getRequest().getHeaders().getFirst(header);
    }

    /**
     * 获得用户User-Agent
     * @return
     */
    public static String getUserAgent(){
        return getHttpHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * 获得用户远程地址
     *
     * @return the string
     */
    public static String getRemoteAddr() {
        String ipAddress = getHttpHeader(GlobalConstant.X_REAL_IP);
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpHeader(GlobalConstant.X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpHeader(GlobalConstant.PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpHeader(GlobalConstant.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpHeader(GlobalConstant.HTTP_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = getHttpHeader(GlobalConstant.HTTP_X_FORWARDED_FOR);
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = Objects.requireNonNull(getRequest().getRemoteAddress()).getAddress().getHostAddress();
        }
        if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = Objects.requireNonNull(getRequest().getRemoteAddress()).getAddress().getHostAddress();
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


}