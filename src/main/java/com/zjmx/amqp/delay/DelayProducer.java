package com.zjmx.amqp.delay;

import com.zjmx.amqp.SerializationUtil;
import com.zjmx.amqp.fourAbstract.message.MyMessage;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
*   延迟队列的生产者
*
*   @Auther Lilymz
*   @Date 2021/11/17/9:24
**/
@Component
public class DelayProducer {
    private static final Logger logger = LoggerFactory.getLogger(DelayProducer.class);
    /***消息模板 *********/
    @Resource
    private RabbitTemplate rabbitTemplate;
    /** 交换机绑定延迟队列 **/
    @Resource(name = "delayDirectBinding")
    private Binding delayDirectBinding;
    /***  延迟交换机 ***/
    @Resource(name = "delayDirectExchange")
    private DirectExchange delayDirectExchange;
    /**
     *  发送一条延迟消息
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/18:19
     *   @params [count]
     *   @return void
     **/
    public void sendMsg(Integer count){
        logger.info("~~~~开始发送延迟消息~~~~~~~");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(getMapper());
        MessageProperties properties = new MessageProperties();
        //设置延迟消息
        properties.setDelay(3);
        //创建消息
        Message message = converter.toMessage(new MyMessage("this is delay message count " + count),properties );
        CorrelationData correlationData = new CorrelationData();
        //发送消息到延迟队列上
        rabbitTemplate.convertAndSend(delayDirectExchange.getName(),delayDirectBinding.getRoutingKey(),message, correlationData);
        //发送日志
        logger.info("发送了一条延迟消息到延迟队列，exchange：{},routekey:{},message:{}",delayDirectExchange.getName()
                ,delayDirectBinding.getRoutingKey(), message);
    }
    private ClassMapper getMapper(){
        DefaultClassMapper  classMapper = new DefaultClassMapper();
        Map<String ,Class<?>> map= new HashMap<>();
        map.put("myMessage",MyMessage.class);
        classMapper.setIdClassMapping(map);
        return classMapper;
    }

}
