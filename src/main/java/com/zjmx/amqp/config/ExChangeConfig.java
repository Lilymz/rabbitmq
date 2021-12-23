package com.zjmx.amqp.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExChangeConfig {
    /**
     *  构建一个简单的广播交换机
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:11
     *   @params []
     *   @return org.springframework.amqp.core.FanoutExchange
     **/
    @Bean
    @Primary
    public FanoutExchange fanoutExchange(){
        return ExchangeBuilder.fanoutExchange("simple_fanout_exchange")
                .durable(true).build();
    }
    /**
     *  构建一个简单的主题交换机
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:13
     *   @params []
     *   @return org.springframework.amqp.core.TopicExchange
     **/
    @Bean
    @Primary
    public TopicExchange topicExchange(){
        return ExchangeBuilder.topicExchange("simple_topic_exchange")
                .durable(true).build();
    }
    /**
     *  构建一个简单的路由交换机
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:13
     *   @params []
     *   @return org.springframework.amqp.core.TopicExchange
     **/
    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange("simple_direct_exchange")
                .durable(true).build();
    }
    /**
     *  构建一个简单的延迟路由交换机
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/17:13
     *   @params []
     *   @return org.springframework.amqp.core.TopicExchange
     **/
    @Bean
    public DirectExchange delayDirectExchange(){
        return ExchangeBuilder.directExchange("simple_delay_direct_exchange")
                .durable(true).build();
    }
}
