package com.winston.utils.imageCode;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName ImageCode
 * @Description 图片验证码类，前后端分离和网页都能用
 * @Author Winston
 * @Date 2019/4/15 21:06
 * @Version 1.0
 **/
@Data
public class ImageCode implements Serializable {

    private static final long serialVersionUID = 1L;

    // 图片
    private BufferedImage image;
    // 随机验证码
    private String code;
    // 过期时间
    private LocalDateTime expireTime;

    private boolean expired;

    public ImageCode(BufferedImage image, String code, int expireIn){
        // int expireIn多少秒之后过期
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    // 判断是否过期
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
