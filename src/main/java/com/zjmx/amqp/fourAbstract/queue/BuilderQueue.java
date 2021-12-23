package com.zjmx.amqp.fourAbstract.queue;

import com.zjmx.amqp.fourAbstract.inface.Queue;
import org.springframework.amqp.core.QueueBuilder;
/**
*   建造队列
*
*   @Auther Lilymz
*   @Date 2021/11/12/15:18
**/
public class BuilderQueue implements Queue {
    /***********            普通队列和延迟队列的对比
     *
     *
     * 惰性队列和普通队列相比，只有很小的内存开销。这里很难对每种情况给出一个具体的数值，但是我们可以类比一下：
     * 当发送1千万条消息，每条消息的大小为1KB，并且此时没有任何的消费者，那么普通队列会消耗1.2GB的内存，而惰性队列只消耗1.5MB的内存。
     *
     * 据官网测试数据显示，对于普通队列，如果要发送1千万条消息，需要耗费801秒，平均发送速度约为13000条/秒。
     * 如果使用惰性队列，那么发送同样多的消息时，耗时是421秒，平均发送速度约为24000条/秒。
     * 出现性能偏差的原因是普通队列会由于内存不足而不得不将消息换页至磁盘。如果有消费者消费时，
     * 惰性队列会耗费将近40MB的空间来发送消息，对于一个消费者的情况，平均的消费速度约为14000条/秒。
     *
     * 如果要将普通队列转变为惰性队列，那么我们需要忍受同样的性能损耗。
     * 当转变为惰性队列的时候，首先需要将缓存中的消息换页至磁盘中，然后才能接收新的消息。
     * 反之，当将一个惰性队列转变为普通队列的时候，和恢复一个队列执行同样的操作，会将磁盘中的消息批量的导入到内存中。
     *
     *
     *
     *
     * ******************/
    /**
     *   创建不带默认实际name队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/14:26
     **/
    @Override
    public org.springframework.amqp.core.Queue create(String name) {
        org.springframework.amqp.core.Queue builderQueue1 = QueueBuilder.nonDurable("builderQueue1").maxLength(3).
            overflow(QueueBuilder.Overflow.rejectPublish).expires(30000).lazy().build();
        return builderQueue1;
    }
    /**
     *   创建带默认实际name队列接口
     *
     *   @Auther Lilymz
     *   @Date 2021/11/12/14:26
     **/
    @Override
    public org.springframework.amqp.core.Queue createDefault(String name) {
        org.springframework.amqp.core.Queue builderQueue2 = QueueBuilder.nonDurable("builderQueue2").autoDelete().maxLength(2).
                overflow(QueueBuilder.Overflow.dropHead).build();
        return builderQueue2;
    }
}
