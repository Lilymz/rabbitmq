package com.zjmx.amqp.fourAbstract.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
/**
*   自定义的消息必须实现序列化接口，不然就会报错
*
*   @Auther Lilymz
*   @Date 2021/11/17/16:56
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMessage implements Serializable{

    private String content;
}
