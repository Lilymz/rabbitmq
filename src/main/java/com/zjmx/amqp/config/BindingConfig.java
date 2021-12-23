package com.zjmx.amqp.config;

import com.zjmx.amqp.fourAbstract.inface.Queue;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.BrokerEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Configuration
public class BindingConfig {
    /**
     *  为广播交换机牵手为normalFanoutQueue的队列
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:32
     *   @params [normalQueue, fanoutExchange]
     *   @return org.springframework.amqp.core.Binding
     **/
//    @Bean
//    public Binding fanoutBinding(Queue normalQueue, FanoutExchange fanoutExchange){
//        return BindingBuilder.bind(normalQueue.create("normalFanoutQueue"))
//                .to(fanoutExchange);
//    }
    /**
     *  为主题交换机牵手为normalTopicQueue的队列
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:32
     *   @params [normalQueue, fanoutExchange]
     *   @return org.springframework.amqp.core.Binding
     **/
//    @Bean
//    public Binding topicBinding(Queue normalQueue, TopicExchange topicExchange){
//        return BindingBuilder.bind(normalQueue.create("normalTopicQueue"))
//                .to(topicExchange).with("*.red.#");
//    }
    /**
     *  为路由交换机牵手为normalQueue的队列
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:32
     *   @params [normalQueue, fanoutExchange]
     *   @return org.springframework.amqp.core.Binding
     **/
    @Bean
    public Binding directBinding(org.springframework.amqp.core.Queue normalDirectQueue, DirectExchange directExchange){
        return BindingBuilder.bind(normalDirectQueue)
                .to(directExchange).with("small.black.dog");
    }
    /**
     *  为路由交换机牵手延迟队列(过期之后放入到普通队列)
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:32
     *   @params [normalQueue, fanoutExchange]
     *   @return org.springframework.amqp.core.Binding
     **/
    @Bean
    public Binding delayDirectBinding(DirectExchange delayDirectExchange, org.springframework.amqp.core.Queue delayDirectQueue){
        //死信队列+死信队列路由key
        return BindingBuilder.bind(delayDirectQueue).to(delayDirectExchange).with("delay.black.dog");
    }

    /***
     *  经纪人事件监听：broker将事件发布到amq.rabbitmq.event topic exchange，每种事件类型具有不同的routing key。
     *  listener 使用event key，该event key用于将AnonymousQueue绑定到exchange，因此listener仅接收选定的事件。
     *  由于这是一个topic exchange，因此可以使用通配符（以及显式请求特定事件）
     *
     * @param pooledChannelConnectionFactory
     * @param connectionListener
     * @return
     */
    @Bean
    public BrokerEventListener brokerEventListener(CachingConnectionFactory pooledChannelConnectionFactory, ConnectionListener connectionListener){
        return new BrokerEventListener(pooledChannelConnectionFactory,"delay.#.dog");
    }

}
