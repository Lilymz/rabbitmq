package com.zjmx.amqp.fourAbstract.inface;

/**
 * 对外提供创建一个amqp -Queue队列
 *    对于真的需要设置参数，那么最好使用构造器模式构建队列，argument:{
 *      x-message-ttl:类似于redis expires
 *      x-expires:过期时间
 *      x-max-length:队列最大容纳数，当超出指定的容纳量则覆盖最前面的，LRU（最近未使用算法）
 *      x-max-length-bytes：队列最大容纳字节量，当超出指定的容纳量则覆盖最前面的，LRU（最近未使用算法）
 *      x-overflow：设置队列溢出行为。这决定了当达到队列的最大长度时，消息会发生什么。有效值为Drop Head或Reject Publish。（达到最大容量量策略）
 *      x-dead-letter-exchange：有时候我们希望当队列的消息达到上限后溢出的消息不会被删除掉，而是走到另一个队列中保存起来
 *      x-dead-letter-routing-key：如果不定义，则默认为溢出队列的routing-key，因此，一般和6一起定义（还不知道有啥用）
 *      x-max-priority：设置队列优先级
 *      x-queue-mode:lazy(懒惰队列尽可能的将消息存到磁盘，当需要使用的使用读到内存)---感觉容易出现读取到内存的时候出错
 *      x-queue-master-locator：设置主定位器模式，该模式确定队列主将位于节点集群上的哪个节点
 *  }
 *
 *
 */
public interface Queue {
    /**
    *   创建不带默认实际name队列接口
    *
    *   @Auther Lilymz
    *   @Date 2021/11/12/14:26
    **/
    org.springframework.amqp.core.Queue create(String name);
    /**
     *   创建带默认实际name队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/14:26
     **/
    org.springframework.amqp.core.Queue createDefault(String name);
}
