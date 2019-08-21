package com.winston.properties;

import lombok.Data;

/**
 * @ClassName ImageCodeProperties
 * @Description 图片验证码配置，可供各项目重用
 * @Author Winston
 * @Date 2019/4/16 20:49
 * @Version 1.0
 **/
@Data
public class ImageCodeProperties {

    // 图片验证码宽度
    private int width = 67;
    // 图片验证码高度
    private int height = 23;
    // 验证码位数
    private int length = 4;
    // 过期时间
    private int expireIn = 60;

    private String url;

}
