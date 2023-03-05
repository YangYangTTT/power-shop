package com.pn.entily;




///此类为  延迟队列  死信交换机   死信队列  以及死信队列路由key的名字
public interface QueueConstant {
    //常量 -- 延迟队列名
    public String ORDER_MS_QUEUE="order.ms.queue";
    //常量 -- 死信交换机名
    public  String ORDER_DEAD_EX="order.dead.ex";
    //常量 -- 死信队列名
    public String ORDER_DEAD_QUEUE = "order.dead.queue";

    //常量 -- 死信队列的路由key
    public String ORDER_DEAD_KEY = "order.dead.key";
}
