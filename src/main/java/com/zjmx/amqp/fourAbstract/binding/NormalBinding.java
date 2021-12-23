package com.zjmx.amqp.fourAbstract.binding;

import com.zjmx.amqp.fourAbstract.inface.Bind;
import org.springframework.amqp.core.Binding;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class NormalBinding implements Bind {
    /**
     * 绑定队列或者交换机所需要用到的参数
     *
     */
    private Map<String ,Object> arguments =new HashMap<>();

    @Override
    public Binding queueBind(String queueName,String routeKey, String exChange) {
        Binding binding =new Binding("queueName", Binding.DestinationType.QUEUE,
                StringUtils.isEmpty(exChange)?"foo.bar":exChange,
                StringUtils.isEmpty(routeKey)?"*.lazy.cat.*":routeKey,arguments);
        return binding;
    }

    @Override
    public Binding exChangeBind(String queueName,String routeKey, String exChange) {
        Binding binding =new Binding(StringUtils.isEmpty(queueName)?"queueName":queueName, Binding.DestinationType.EXCHANGE,
                StringUtils.isEmpty(exChange)?"foo.bar":exChange,
                StringUtils.isEmpty(routeKey)?"*.lazy.cat.*":routeKey,arguments);
        return binding;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
