package com.winston.properties;

import lombok.Data;

/**
 * @ClassName BrowserProperties
 * @Description
 * @Author Winston
 * @Date 2019/4/14 23:21
 * @Version 1.0
 **/
@Data
public class BrowserProperties {

    // 配置文件配置了就用，没配置就用默认的
    private String loginPage = "/login";

    private LoginType loginType = LoginType.JSON;

    private String loginTag = "web";
}
