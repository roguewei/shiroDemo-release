package com.winston.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SecurityProperties
 * @Description
 * @Author Winston
 * @Date 2019/4/14 23:21
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "winston.security")
@Data
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    private WechatProperties wechat = new WechatProperties();

    /***
     * token配置
     */
    private JwtProperties jwt = new JwtProperties();

}
