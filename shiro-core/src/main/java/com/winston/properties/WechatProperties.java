package com.winston.properties;

import lombok.Data;

/**
 * @author Winston
 * @title: WechatProperties
 * @projectName shiro-parent
 * @description:
 * @date 2019/7/15 9:44
 */
@Data
public class WechatProperties {

    private String token;
    private String appId;
    private String secret;
    private String redirectUrl;
    private String responseType;
    private String scope;

}
