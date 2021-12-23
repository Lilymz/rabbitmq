package com.zjmx.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 *  rabbitmq 配置常量
 * @author 26340
 */
public class RabbitMqConstant {

    public static void initConfig(CachingConnectionFactory factory){
        factory.setHost("139.196.242.100");
        factory.setUsername("zjmx");
        factory.setPassword("123456");
        factory.setVirtualHost("/switch");
    }
}
