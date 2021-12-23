package com.zjmx.amqp.connAndResources;

import com.zjmx.amqp.RabbitMqConstant;
import com.zjmx.amqp.fourAbstract.factory.QueueFactory;
import com.zjmx.amqp.fourAbstract.inface.Queue;
import com.zjmx.amqp.fourAbstract.message.MyMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

/**
 *  测试带缓存的连接工厂测试
 *
 */
public class CachingConnectionFactoryTest {

    public static void main(String[] args) throws InterruptedException {
        //初始化配置
        CachingConnectionFactory factory=new CachingConnectionFactory();
        RabbitMqConstant.initConfig(factory);
        Queue myQueue = QueueFactory.getMyQueue(QueueFactory.Builder.BuilderQueue);
        org.springframework.amqp.core.Queue queue = myQueue.create("");
        //指定是哪个消息队列
        RabbitAdmin amqp = new RabbitAdmin(factory);
        amqp.declareQueue(queue);
        //指定消息模板
        RabbitTemplate rabbitTemplate = amqp.getRabbitTemplate();
        int count = 0;
        while (true){
            Thread.sleep(500);
            count++;
            SimpleMessageConverter converter = new SimpleMessageConverter();
            Message message = converter.toMessage(new MyMessage("hello"), new MessageProperties());
            rabbitTemplate.convertAndSend(queue.getName(),message);
            System.out.println(message.getBody()+"count "+count);
        }
    }
}
