package com.winston.rabbitmq;

import com.winston.config.MQConfig;
import com.winston.utils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Winston
 * @title: MQSender
 * @projectName shiroDemo
 * @description: 消息队列发送至
 * @date 2019/7/30 9:50
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendExample(MQMessage mqMessage){
        String msg = RedisService.beanToString(mqMessage);
        log.info("send message is : " + msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME, msg);
    }

}
