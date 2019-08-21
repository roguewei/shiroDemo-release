package com.winston.utils.wechat;

import lombok.Data;

/**
 * @author Winston
 * @title: TemplateParam
 * @projectName shiro-parent
 * @description: TODO
 * @date 2019/7/2113:34
 */
@Data
public class TemplateParam {
    // 参数名称
    private String name;
    // 参数值
    private String value;
    // 颜色
    private String color;

    public TemplateParam(String name, String value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }
}

