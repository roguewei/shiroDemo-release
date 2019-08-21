package com.winston.rabbitmq;

import com.winston.entity.User;
import lombok.Data;

/**
 * @author Winston
 * @title: MQMessage
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/30 9:49
 */
@Data
public class MQMessage {

    private User user;

    private String msg;

}
