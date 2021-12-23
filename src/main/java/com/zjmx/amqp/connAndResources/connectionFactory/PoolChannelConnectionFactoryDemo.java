package com.zjmx.amqp.connAndResources.connectionFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;

/**
*   这个工厂基于Apache Pool2实现，管理着单个 connection 和两个 channel 池，
 *   两个channel 池 其中一个用于事务通道，另一个用于非事务通道，这些池都有默认的配置`GenericObjectPool`
 *   （这是`commons-pool2`中的类）；同时提供了一个 回调函数配置池。
*      使用这个链接必须依赖
 *      <dependency>
 *     <groupId>org.apache.commons</groupId>
 *     <artifactId>commons-pool2</artifactId>
 *       </dependency>
*   @Auther Lilymz
*   @Date 2021/11/16/15:17
*   @params
*   @return
**/
public class PoolChannelConnectionFactoryDemo {
    /**
     * （1个连接池，两个channel池）
     * PoolChannel链接工厂日志
     */
    private static Logger logger = LoggerFactory.getLogger(PoolChannelConnectionFactoryDemo.class);
    public static void main(String[] args) throws InterruptedException {
        ConnectionFactory initialConn = new ConnectionFactory();
        initialConn.setUsername("zimx");
        initialConn.setPassword("123456");
        initialConn.setVirtualHost("/switch");
        initialConn.setAutomaticRecoveryEnabled(true);
        PooledChannelConnectionFactory factory = new PooledChannelConnectionFactory(initialConn);
        factory.setPoolConfigurer((channel,tx)->{
            //配置channel连接池的缓存大小
            GenericObjectPoolConfig<Channel> objectGenericObjectPoolConfig = new GenericObjectPoolConfig<>();
            objectGenericObjectPoolConfig.setMinIdle(0);
            objectGenericObjectPoolConfig.setMaxIdle(2);
            objectGenericObjectPoolConfig.setMaxTotal(2);
            //链接池公平
            objectGenericObjectPoolConfig.setFairness(true);
            objectGenericObjectPoolConfig.setJmxEnabled(true);
            //设置连接池配置
            channel.setConfig(objectGenericObjectPoolConfig);
            if (tx){
                //事务配置
                return;
            }
            //非事务配置
        });
    }
}
