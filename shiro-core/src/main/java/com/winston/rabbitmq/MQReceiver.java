package com.winston.rabbitmq;

import com.winston.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Winston
 * @title: MQReceiver
 * @projectName shiroDemo
 * @description: 消息队列消费者
 * @date 2019/7/30 9:52
 */
@Service
@Slf4j
public class MQReceiver {

    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receiveExample(String message){
        log.info(message);
        // 处理具体业务逻辑
    }

}
