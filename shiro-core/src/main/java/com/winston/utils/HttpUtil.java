package com.winston.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Winston
 * @title: HttpUtil
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 11:45
 */
public class HttpUtil {

    /**
      * @Author Winston
      * @Description 通过request获取uri
      * @Date 11:45 2019/7/24
      * @Param
      * @return
      **/
    public static String getRequestUrl(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return req.getRequestURI();
    }

    /**
      * @Author Winston
      * @Description 获取当前请求的request
      * @Date 11:45 2019/7/24
      * @Param
      * @return
      **/
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    /**
      * @Author Winston
      * @Description 获取请求客户端Ip地址
      * @Date 11:46 2019/7/24
      * @Param
      * @return
      **/
    public static String getIpAdrress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
