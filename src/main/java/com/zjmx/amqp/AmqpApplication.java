package com.zjmx.amqp;

import com.zjmx.amqp.delay.DelayCustomer;
import com.zjmx.amqp.delay.DelayProducer;
import com.zjmx.amqp.fourAbstract.inface.Queue;
import com.zjmx.amqp.fourAbstract.factory.QueueFactory;
import com.zjmx.amqp.fourAbstract.message.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@EnableScheduling
@SpringBootApplication
public class AmqpApplication/* implements CommandLineRunner*/ {
    private static final Logger logger = LoggerFactory.getLogger(AmqpApplication.class);
    @Resource
    private DelayProducer delayProducer;
    @Resource
    private DelayCustomer delayCustomer;
    private Integer count =0;
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AmqpApplication.class, args);

    }

    /**
     * 生产消息
     */
    @Scheduled(fixedDelay = 3000)
    public void producer3(){//带连接池事务通道
        count++;
        delayProducer.sendMsg(count);
    }

//    /**
//     * 消费消息
//     */
//    @RabbitListener(queues = "normalDirectQueue")
//    public void customer(Message message){//带连接池事务通道
//        logger.info("~~~~~~~~~~~~~~normalDirectQueue开始消费~~~~~~~~~~~~~~~~~");
//        System.out.println("消费一条来自delayDirectQueue队列的消息，内容是："+new String(message.getBody()));
//        logger.info("~~~~~~~~~~~~~~normalDirectQueue结束消费~~~~~~~~~~~~~~~~~,是否属于延迟消息{}",message.getMessageProperties().getReceivedDelay());
////        delayCustomer.customerMsg();
//    }
}
