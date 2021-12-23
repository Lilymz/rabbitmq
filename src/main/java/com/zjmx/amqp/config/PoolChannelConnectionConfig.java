package com.zjmx.amqp.config;

import com.rabbitmq.client.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplateBuilder;

import java.util.Collections;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
*   rabbitmq链接配置
*
*   @Auther Lilymz
*   @Date 2021/11/17/11:32
**/
@Configuration
@EnableRabbit
public class PoolChannelConnectionConfig {
    private static final Logger logger = LoggerFactory.getLogger(PoolChannelConnectionConfig.class);
    /**
     *  构建一个在在发生运行时异常，每秒重试的时间1分钟，在第一次重试后不超过4分钟则允许重试
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/14:08
     *   @params []
     *   @return org.springframework.amqp.rabbit.core.RabbitAdmin
     **/
    @Bean(value = "rabbitAdmin1")
    public RabbitAdmin rabbitAdmin1(ConnectionListener connectionListener){
        RabbitAdmin  rabbitAdmin =new RabbitAdmin(getPoolChannelFactory(connectionListener));
        RetryTemplateBuilder retryBuilder = new RetryTemplateBuilder();
        //在什么异常重试
        retryBuilder.retryOn(RuntimeException.class);
        retryBuilder.withinMillis(4);
        retryBuilder.fixedBackoff(MINUTES.toMillis(3));
        rabbitAdmin.setRetryTemplate(retryBuilder.build());
        return rabbitAdmin;
    }
    /**
     *  获取发送接收模板
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/14:11
     *   @params []
     *   @return org.springframework.amqp.rabbit.core.RabbitTemplate
     **/
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionListener connectionListener){
        RabbitTemplate rabbitTemplate = rabbitAdmin1(connectionListener).getRabbitTemplate();
        rabbitTemplate.setReturnsCallback((returnedMessage -> {
            logger.info("返回的消息:{}",new String(returnedMessage.getMessage().getBody()));
        }));
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            logger.error("确认消息：回复原因{}，routekey：{},ack:{},造成的原因:{}",correlationData.getReturned().getReplyText(),correlationData.getReturned().getRoutingKey(),ack,cause);
        });
        return rabbitTemplate;
    }
    /**
     *  获取发送接收模板
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/14:11
     *   @params []
     *   @return org.springframework.amqp.rabbit.core.RabbitTemplate
     **/
    @Bean
    public RabbitTemplate rabbitTemplate2(CachingConnectionFactory getPoolChannelFactory){
        RabbitTemplate rabbitTemplate =new RabbitTemplate(getPoolChannelFactory);
        rabbitTemplate.setReturnsCallback((returnedMessage -> {
            logger.info("返回的消息:{}",new String(returnedMessage.getMessage().getBody()));
        }));
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            logger.error("确认消息：回复原因{}，routekey：{},ack:{},造成的原因:{}",correlationData.getReturned().getReplyText(),correlationData.getReturned().getRoutingKey(),ack,cause);
        });
        return rabbitTemplate;
    }
    /**
     *  配置一个rabbitmq工厂：该工厂可以连接管理一个连接池+一个非事务池+一个事务池（用于需要加事务的通道）
     *
     *   @Auther Lilymz
     *   @Date 2021/11/17/11:47
     *   @params []
     *   @return org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory
     *
     *   CacheMode.CHANNEL 属性：
     *
     *  Property	Meaning
     *  connectionName	ConnectionNameStrategy生成的 connection name。
     *  channelCacheSize	当前配置的允许空闲的最大channel数。
     *  localPort	连接的本地端口（如果有）。这可用于与RabbitMQ Admin UI上的connection和channel关联。
     *  idleChannelsTx	当前空闲（缓存）的事务channel的数量。
     *  idleChannelsNotTx	当前空闲（缓存）的非事务channel的数量。
     *  idleChannelsTxHighWater	同时空闲（缓存）的最大事务channel数。
     *  idleChannelsNotTxHighWater	同时空闲（缓存）的最大非事务channel数。
     *
     *  CacheMode.CONNECTION 属性：
     *
     *  Property	Meaning
     *  connectionName:<localPort>	ConnectionNameStrategy生成的 connection name。
     *  openConnections	代理连接的连接对象的数量
     *  channelCacheSize	当前配置的允许空闲的最大channel数。
     *  connectionCacheSize	当前配置的允许空闲的最大连接数。
     *  idleConnections	当前空闲的连接数。
     *  idleConnectionsHighWater	并发空闲的最大连接数。
     *  idleChannelsTx:<localPort>	该连接当前空闲（缓存）的事务channel的数量。可以使用属性名称的localPort部分与RabbitMQ Admin UI上的connection和channel关联。
     *  idleChannelsNotTx:<localPort>	该连接当前空闲（缓存）的非事务channel的数量。可以使用属性名称的localPort部分与RabbitMQ Admin UI上的connection和channel关联。
     *  idleChannelsTxHighWater:<localPort>	同时空闲（缓存）的最大事务channel数。属性名称的localPort部分可用于与RabbitMQ Admin UI上的connection和channel关联。
     *  idleChannelsNotTxHighWater:<localPort>	同时空闲（缓存）的最大非事务channel数。属性名称的localPort部分可用于与RabbitMQ Admin UI上的connection和channel关联。
     **/
    @Bean
    @ConditionalOnMissingBean(value = org.springframework.amqp.rabbit.connection.ConnectionFactory.class)
    public CachingConnectionFactory getPoolChannelFactory(ConnectionListener connectionListener){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        //连接监听
        factory.setConnectionListeners(Collections.singletonList(connectionListener));
        //设置mq地址
        factory.setAddressResolver(()-> Collections.singletonList(new Address("139.196.242.100",5672)));
        //连接
        factory.setUsername("zjmx");
        factory.setPassword("123456");
        factory.setVirtualHost("/switch");
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        factory.setPublisherReturns(true);
        factory.setConnectionLimit(4);
        factory.setConnectionCacheSize(4);
        factory.setChannelCacheSize(3);
        factory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return factory;
    }
//    @Bean
//    @ConditionalOnMissingBean(value = org.springframework.amqp.rabbit.connection.ConnectionFactory.class)
//    public PooledChannelConnectionFactory getPoolChannelFactory(ConnectionListener connectionListener){
//        //初始配置
//        ConnectionFactory rabbitFactory =new ConnectionFactory();
//        rabbitFactory.setUsername("zjmx");
//        rabbitFactory.setPassword("123456");
//        rabbitFactory.setHost("139.196.242.100");
//        rabbitFactory.setVirtualHost("/switch");
//        rabbitFactory.setChannelRpcTimeout((int) MINUTES.toMillis(10));
//        rabbitFactory.setAutomaticRecoveryEnabled(false);
//        rabbitFactory.setChannelShouldCheckRpcResponseType(true);
//        //工作队列情况的处理（没有给定qos消费不玩的情况，一直占用连接+通道，给定超时）
//        rabbitFactory.setWorkPoolTimeout((int)MINUTES.toMillis(6));
//        //e.isHardError()连接错误未true
//        Predicate<ShutdownSignalException> conditionRecovery = e -> !e.isHardError();
//        //是否触发连接恢复
//        rabbitFactory.setConnectionRecoveryTriggeringCondition(conditionRecovery);
//        //连接恢复间隔
//        rabbitFactory.setNetworkRecoveryInterval(MINUTES.toMillis(2));
//        //这里其实是设置账号密码（看看前面设置的会不会被覆盖：确认过了，会覆盖）
//        DefaultCredentialsProvider provider =new DefaultCredentialsProvider("zjmx","123456");
//        rabbitFactory.setCredentialsProvider(provider);
//        //装饰模式  ：管理着单个 connection 和两个 channel 池(非事务池+事务池)|必须引入common-pool依赖
//        PooledChannelConnectionFactory enabledTXFactory = new PooledChannelConnectionFactory(rabbitFactory);
//        //是否进行发布确认
//        enabledTXFactory.setSimplePublisherConfirms(true);
//        enabledTXFactory.setConnectionNameStrategy(factory->"poolChannelConnection"+ StringUtils.leftPad(String.valueOf(new Random().nextInt(9))
//                ,new Random().nextInt(3), "0"));
//       ChannelListener channelListener = (channel,transactional)->{
//            System.out.println("信道的编号："+channel.getChannelNumber()+"，当前信道是否是在事务信道池中："+transactional);
//        };
//        //添加连接的监听和信道的监听
//        enabledTXFactory.setConnectionListeners(Arrays.asList(connectionListener));
//        enabledTXFactory.setChannelListeners(Arrays.asList(channelListener));
//        //定义客户端属性然后转发到thing1
//        enabledTXFactory.getRabbitConnectionFactory().getClientProperties().put("thing1", "thing2");
//        //测试发布消息确认，template那边对丢弃的消息的跟踪
//        enabledTXFactory.setSimplePublisherConfirms(true);
//        //这里现在还不是很理解暂时注释掉
//        enabledTXFactory.setPoolConfigurer((channel,tx)->{
//            //配置channel连接池的缓存大小
//            GenericObjectPoolConfig<Channel> objectGenericObjectPoolConfig = new GenericObjectPoolConfig<>();
//            objectGenericObjectPoolConfig.setMaxIdle(8);
//            objectGenericObjectPoolConfig.setMaxTotal(8);
//            objectGenericObjectPoolConfig.setMinIdle(0);
//            channel.setConfig(objectGenericObjectPoolConfig);
//            if (tx){
//                //事务channel
//            }
//            //非事务
//        });
//        return enabledTXFactory;
//    }
    @Bean
    public ConnectionListener connectionListener(){
        return (connection -> System.out.println("连接被创建了,本地连接的端口："+connection.getLocalPort()+"主机名："+connection.getDelegate().getAddress().getHostAddress()));
    }
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionListener connectionListener){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(getPoolChannelFactory(connectionListener));
        //设置并发情况下的消费者
        factory.setConcurrentConsumers(3);
        //设置并发情况下最大消费者个数
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    /**
     *  设置消息监听容器，用于消息的消费(这个作用于全局)
     *
     * @param getPoolChannelFactory
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(CachingConnectionFactory getPoolChannelFactory){
        //给消息监听的容器指定连接工厂
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(getPoolChannelFactory);
        //监听指定的队列
        simpleMessageListenerContainer.setQueueNames("normalDirectQueue");
        simpleMessageListenerContainer.setConcurrentConsumers(2);
        simpleMessageListenerContainer.setConcurrency("2-5");
        simpleMessageListenerContainer.setMessageListener(msg->{
            System.out.println(new String(msg.getBody()));
        });
        simpleMessageListenerContainer.setReceiveTimeout(3000);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        simpleMessageListenerContainer.setAutoStartup(true);
        simpleMessageListenerContainer.setGlobalQos(true);
        return simpleMessageListenerContainer;
    }

}
