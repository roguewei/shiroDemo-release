package com.winston.properties;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class JwtProperties {
    /**
     * JwtToken 默认过期时间（ms）
     */
    private long tokenExpirationTime = 60 * 60 * 1000;
//    private long tokenExpirationTime = 1000L * 60 * 5;

    /**
     * Token issuer. 默认发行者
     */
    private String tokenIssuer = "winston-pms";

    /**
     * Key is used to sign JwtToken. 发行签名
     */
    private String tokenSigningKey = "0c34c2245a035786e936fec8b7e93541";

    /***
     * 默认前缀
     */
    private String redisPrefix = "fswx";

    /***
     * 默认请求头参数名
     */
    private String header = "authorization";

    /***
     * 默认计算时间单位
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
}