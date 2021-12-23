package com.zjmx.amqp.fourAbstract.queue;


import org.springframework.amqp.core.Queue;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class NormalQueue implements com.zjmx.amqp.fourAbstract.inface.Queue {
    /**
     *   创建不带默认实际name队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/14:26
     **/
    @Override
    public Queue create(String name) {
        //创建队列实例
        org.springframework.amqp.core.Queue queue =new org.springframework.amqp.core.Queue(StringUtils.isEmpty(name)?"normalQueue1":name
                ,true,false,false);
        queue.setActualName("defined by myself"+name);
        return queue;
    }
    /**
     *   创建带默认实际name队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/14:26
     **/
    @Override
    public Queue createDefault(String name) {
        //创建队列实例
        org.springframework.amqp.core.Queue queue =new org.springframework.amqp.core.Queue(StringUtils.isEmpty(name)
                ? "normalQueue2":name,false,false,true);
        queue.setLeaderLocator("leader-fanout");
        System.out.println("current queue actual name is :"+queue.getActualName());
        return queue;
    }
}
