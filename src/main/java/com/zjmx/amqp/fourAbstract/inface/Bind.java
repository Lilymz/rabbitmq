package com.zjmx.amqp.fourAbstract.inface;

import org.springframework.amqp.core.Binding;

/**
 * 鉴于生产者发送到交换而消费者从队列接收，将队列连接到交换的绑定对于通过消息连接这些生产者和消费者至关重要。
 *
 * 在 Spring AMQP 中，我们定义了一个Binding类来表示这些连接。本节回顾将队列绑定到交换的基本选项。
 */
public interface Bind {
    /**
    *   构建一个队列绑定 Queue
    *
    *   @Auther Lilymz
    *   @Date 2021/11/12/15:39
    **/
    Binding queueBind(String queueName,String routeKey,String exChange);

    /**
     *   构建一个队列绑定 ExChange
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/15:39
     **/
    Binding exChangeBind(String queueName,String routeKey,String exChange);
}
