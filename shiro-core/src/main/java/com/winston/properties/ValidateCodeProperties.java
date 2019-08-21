package com.winston.properties;

import lombok.Data;

/**
 * @ClassName ValidateCodeProperties
 * @Description 验证码配置类
 * @Author Winston
 * @Date 2019/4/16 20:51
 * @Version 1.0
 **/
@Data
public class ValidateCodeProperties {

    private ImageCodeProperties image = new ImageCodeProperties();

}
