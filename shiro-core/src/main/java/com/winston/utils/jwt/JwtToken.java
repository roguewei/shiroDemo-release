package com.winston.utils.jwt;

import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public interface JwtToken {
    String getToken();

    /**
     * @return a
     * @Author weigaosheng
     * @Description 通过token获取整个claims体，可以通过claims获取其他信息
     * @Date 8:59 2018/12/2
     * @Param httpServletRequest
     **/
    Claims getClaims();

    /**
     * @return a
     * @Author weigaosheng
     * @Description 通过token获取userid
     * @Date 8:58 2018/12/2
     * @Param httpServletRequest
     **/
    Integer getUserId();

    String getUserName();

}
