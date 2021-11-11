package com.somnus.microservice.commons.core.utils;

import com.somnus.microservice.commons.base.constant.GlobalConstant;
import com.somnus.microservice.commons.base.dto.LoginAuthDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            loginAuthDto = new LoginAuthDto();
        } catch (Exception e){
            log.error("未能正确获取登陆用户信息" , e);
            loginAuthDto = new LoginAuthDto(0L, "admin", "非正常用户");
        }
        return loginAuthDto;
    }

    /**
     * Gets login auth dto.
     *
     * @return the login auth dto
     */
//    public static LoginAuthDto getLoginUser() {
//        LoginAuthDto loginAuthDto;
//        try{
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            List<String> roles = (List)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//            loginAuthDto = new LoginAuthDto(0L, userDetails.getUsername(), userDetails.getPassword());
//            log.info("username:{}---->roles:{}", userDetails.getUsername(), roles);
//        } catch (Exception e){
//            log.error("未能正确获取登陆用户信息" , e);
//            loginAuthDto = new LoginAuthDto(0L, "admin", "非正常用户");
//        }
//        return loginAuthDto;
//    }


}