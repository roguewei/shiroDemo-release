package com.winston.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Winston
 * @title: ShiroCoreConfig
 * @projectName jujia
 * @description:
 * @date 2019/7/5 17:45
 */
@Configuration
// 让读取配置文件的SecurityProperties类生效
@EnableConfigurationProperties(SecurityProperties.class)
public class ShiroCoreConfig {
}
