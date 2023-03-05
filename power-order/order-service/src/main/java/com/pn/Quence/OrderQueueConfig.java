package com.pn.Quence;


import com.pn.entily.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//此类配置交换机   死信队列 等信息
@Configuration
public class OrderQueueConfig {
    //配置死信交换机(直连类型的交换机),交换机名称为order.dead.ex
    @Bean
    public DirectExchange  orderDeadEx(){
        return  new  DirectExchange(QueueConstant.ORDER_DEAD_EX);
    }
    //配置死信队列,队列名称为order.dead.queue
    @Bean
    public Queue orderDeadQueue(){
        return  new Queue(QueueConstant.ORDER_DEAD_QUEUE);
    }
    //将死信队列和死信交换机绑定,同时给死信队列指定路由key为order.dead.key
    @Bean
    //bindingbuilder.bind 队列 .to交换机.with key
    public Binding deadQueueBindK(){
      return BindingBuilder.bind(orderDeadQueue()).to(orderDeadEx()).with(QueueConstant.ORDER_DEAD_KEY);
    }

    //配置延迟队列,队列名称为order.ms.queue
    @Bean
    public Queue orderMsQueue(){
Map<String,Object> args=new HashMap<>();
//延迟时间(2分钟,一般是15分钟)
        args.put("x-message-ttl",1000*120);
        //时间到后消息转到死信交换机
        args.put("x-dead-letter-exchange",QueueConstant.ORDER_DEAD_EX);
        //时间到后消息转到路由order.dead.key绑定的死信队列
        args.put("x-dead-letter-routing-key", QueueConstant.ORDER_DEAD_KEY);
        //true(持久化延迟队列),false(不独占延迟队列),false(不自动删除延迟队列)
        return  new Queue(QueueConstant.ORDER_MS_QUEUE,true,false,false,args);

    }
}
