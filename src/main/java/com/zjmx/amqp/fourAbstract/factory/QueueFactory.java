package com.zjmx.amqp.fourAbstract.factory;

import com.zjmx.amqp.fourAbstract.inface.Queue;
import com.zjmx.amqp.fourAbstract.queue.BuilderQueue;
import com.zjmx.amqp.fourAbstract.queue.NormalQueue;

public class QueueFactory {
    /**
    *   需要工厂生产的队列类型
    *
    *   @Auther Lilymz
    *   @Date 2021/11/12/15:30
    **/
    public interface Builder{
        Integer NormalQueue=1;
        Integer BuilderQueue = 2;
    }
    /**
    *   拿取一个指定类型的队列
    *
    *   @Auther Lilymz
    *   @Date 2021/11/12/15:30
    *   @params
    *   @return
    **/
    public static Queue getMyQueue(Integer type){
        if (Builder.NormalQueue.equals(type)){
            return new NormalQueue();
        }else if (Builder.BuilderQueue.equals(type)){
            return new BuilderQueue();
        }
        return null;
    }
}
