package com.pn.listener;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.entily.ChangeStock;
import com.pn.entily.Order;
import com.pn.entily.QueueConstant;
import com.pn.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

//监听队列
@Component
public class OrderDeadListener {
    //注入orderservice(用于根据订单号查询订单)
    @Autowired
    private OrderService orderService;
    //监听名称为order.dead.queue的死信队列,即从死信队列中拿到消息并处理
    @RabbitListener(queues = QueueConstant.ORDER_DEAD_QUEUE)
    public  void handlerOrderDeadMsg(Message message, Channel channel){
        /*
          拿到消息:
         */
        String jsonStr = new String(message.getBody());
        //转换为普通格式
        JSONObject jsonObj  = JSONObject.parseObject(jsonStr);
        //取出订单号
        String orderNum =jsonObj.getString("orderNum");
        //去除changeStock对象
        ChangeStock changeStock = jsonObj.getObject("changeStock", ChangeStock.class);
        System.out.println("订单号为："+orderNum);

        ///根据订单号拿到订单
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNumber, orderNum)
        );
        //判断订单中的支付状态（已支付 -》签收，未支付-》回查支付平台，回查支付则改支付状态，回查未支付则回滚）
             if(order.getIsPayed().equals(1)){//已支付情况
                 //签收消息
                 try {
                     channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                    return;
             }
             ///未支付情况  （先回查支付平台）
            //1：查询支付平台显示已支付  则修改支付状态为2
          //2：查询支付平台显示未支付   则调用回滚方法   调用完之后 签收消息
        orderService.orderRollback(order,changeStock);//调用消息回滚方法
       //回滚之后签收消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
