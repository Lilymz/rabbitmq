package com.zjmx.amqp.fourAbstract.binding;

import com.zjmx.amqp.fourAbstract.factory.QueueFactory;
import com.zjmx.amqp.fourAbstract.inface.Bind;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;
/**
*   构建队列或者交换机绑定
*
*   @Auther Lilymz
*   @Date 2021/11/12/16:45
**/
public class BuilderBinding implements Bind {
    /**
     * 绑定队列或者交换机所需要用到的参数
     *
     */
    private Map<String ,Object> arguments =new HashMap<>();

    @Override
    public Binding queueBind(String queueName,String routeKey, String exChange) {
        Exchange build = ExchangeBuilder.directExchange(exChange).build();
        Binding binding = BindingBuilder.bind(QueueFactory.getMyQueue(QueueFactory.Builder.BuilderQueue)
                .create(queueName)).to(build).with(routeKey).and(getArguments());
        return binding;
    }

    @Override
    public Binding exChangeBind(String queueName,String routeKey, String exChange) {
        Exchange build1 = ExchangeBuilder.fanoutExchange(exChange).build();
        Exchange build = ExchangeBuilder.directExchange(exChange).build();
        Binding binding = BindingBuilder.bind(build).to(build1).with(routeKey).and(getArguments());
        return binding;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
