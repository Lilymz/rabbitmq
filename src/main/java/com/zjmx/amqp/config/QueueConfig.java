package com.zjmx.amqp.config;

import com.zjmx.amqp.fourAbstract.factory.QueueFactory;
import com.zjmx.amqp.fourAbstract.inface.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class QueueConfig {
    /**
     *  非建构器构建的队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:21
     *   @params []
     *   @return com.zjmx.amqp.fourAbstract.inface.Queue
     **/
    @Bean
    public Queue normalQueue(){
        return QueueFactory.getMyQueue(QueueFactory.Builder.NormalQueue);
    }
    /**
     *  建构器构建的队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:21
     *   @params []
     *   @return com.zjmx.amqp.fourAbstract.inface.Queue
     **/
    @Bean
    public Queue builderQueue(){
        return QueueFactory.getMyQueue(QueueFactory.Builder.NormalQueue);
    }
    @Bean
    public org.springframework.amqp.core.Queue normalDirectQueue(Queue normalQueue){
        return normalQueue.create("normalDirectQueue");
    }
    @Bean
    public org.springframework.amqp.core.Queue delayDirectQueue(DirectExchange directExchange, Binding directBinding){
        return  QueueBuilder.durable("delayDirectQueue").maxLength(1000).autoDelete().
                overflow(QueueBuilder.Overflow.rejectPublish).ttl((int) TimeUnit.SECONDS.toMillis(10))
                .deadLetterExchange(directExchange.getName()).deadLetterRoutingKey(directBinding.getRoutingKey()).build();
    }
}
