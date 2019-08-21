package com.winston.utils.wechat;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

import java.util.List;

/**
 * @author Winston
 * @title: WeChatUser
 * @projectName shiro-parent
 * @description: 通过网页授权获取的用户信息
 * @date 2019/7/15 10:22
 */
@Data
public class WeChatUser extends UsernamePasswordToken {

    // 用户标识
    private String openId;
    // 用户昵称
    private String nickname;
    // 性别（1是男性，2是女性，0是未知）
    private int sex;
    // 国家
    private String country;
    // 省份
    private String province;
    // 城市
    private String city;
    // 用户头像链接
    private String headImgUrl;
    // 用户特权信息
    private List<String> privilegeList;

    private String unionid;

}
