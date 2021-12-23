package com.zjmx.amqp.delay;

import com.zjmx.amqp.fourAbstract.message.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
*   延迟队列消费者（时间到了一定程序去执行的队列）
*
*   @Auther Lilymz
*   @Date 2021/11/17/9:24
**/
@Component
public class DelayCustomer {
    /***延迟队列的消息日志**/
    private static final Logger logger = LoggerFactory.getLogger(DelayCustomer.class);
    /***装配消息模板**/
    @Resource(name = "rabbitTemplate2")
    RabbitTemplate rabbitTemplate2;
    @Resource
    private Queue normalDirectQueue;
    /**
     *  消费者去消费消息（如果delayDirectQueue没有一个是消息过期那他就不会进入到死信队列normalDirectQueue也就不会被以下方法消费，没有消息数据）
     *
     *   @Auther Lilymz
     *   @Date 2021/11/18/13:50
     *   @params []
     *   @return void
     **/
    public void customerMsg(){
        //获取到消息，这里我们可以直接转化成我们想要的类型，因为spring底层帮我们做了（SimpleMessageConvert）
//        new Thread(()->{
                MyMessage message = (MyMessage)rabbitTemplate2.receiveAndConvert(normalDirectQueue.getName(), TimeUnit.SECONDS.toMillis(10));
            System.out.println("线程："+Thread.currentThread().getName()+"消费消息："+message.getContent());
//        }).start();
//
//        new Thread(()->{
//            MyMessage message = (MyMessage)rabbitTemplate2.receiveAndConvert(normalDirectQueue.getName(), TimeUnit.SECONDS.toMillis(10));
//            System.out.println("线程："+Thread.currentThread().getName()+"消费消息"+message.getContent());
//        }).start();
//        new Thread(()->{
//            MyMessage message = (MyMessage)rabbitTemplate2.receiveAndConvert(normalDirectQueue.getName(), TimeUnit.SECONDS.toMillis(10));
//            System.out.println("线程："+Thread.currentThread().getName()+"消费消息"+message.getContent());
//        }).start();
//        new Thread(()->{
//            MyMessage message = (MyMessage)rabbitTemplate2.receiveAndConvert(normalDirectQueue.getName(), TimeUnit.SECONDS.toMillis(10));
//            System.out.println("线程："+Thread.currentThread().getName()+"消费消息"+message.getContent());
//        }).start();
//        new Thread(()->{
//            MyMessage message = (MyMessage)rabbitTemplate2.receiveAndConvert(normalDirectQueue.getName(), TimeUnit.SECONDS.toMillis(10));
//            System.out.println("线程："+Thread.currentThread().getName()+"消费消息"+message.getContent());
//        }).start();
//        logger.info("~~~~~~~~~~~~~~normalDirectQueue开始消费~~~~~~~~~~~~~~~~~");
//        System.out.println("消费一条来自delayDirectQueue队列的消息，内容是："+message.getContent());
//        logger.info("~~~~~~~~~~~~~~normalDirectQueue结束消费~~~~~~~~~~~~~~~~~");
    }
}
