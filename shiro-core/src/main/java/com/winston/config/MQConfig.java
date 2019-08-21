package com.winston.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Winston
 * @title: MQConfig
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/30 9:44
 */
@Configuration
public class MQConfig {

    public static final String QUEUE_NAME = "winston.queue";

    @Bean
    public Queue winstonQueye(){
        return new Queue(QUEUE_NAME, true);
    }

}
