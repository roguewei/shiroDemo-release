package com.winston.utils.wechat;

import lombok.Data;

/**
 * @author Winston
 * @title: Oauth2Token
 * @projectName shiro-parent
 * @description: 网页授权信息
 * @date 2019/7/15 10:23
 */
@Data
public class Oauth2Token {

    // 网页授权接口调用凭证
    private String accessToken;
    // 凭证有效时长
    private int expiresIn;
    // 用于刷新凭证
    private String refreshToken;
    // 用户标识
    private String openId;
    // 用户授权作用域
    private String scope;

}
