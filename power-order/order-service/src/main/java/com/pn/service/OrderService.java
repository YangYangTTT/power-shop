package com.pn.service;

import com.pn.entily.ChangeStock;
import com.pn.entily.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pn.entily.OrderConfirmDto;
import com.pn.entily.OrderStatusCount;
import com.pn.vo.OrderConfirmVo;

public interface OrderService extends IService<Order>{
////查询用户所有订单的各个状态的业务方法
    public OrderStatusCount   selectStatusCount(String userId);


    //确认订单的业务方法    入参有
    //参数1：String userId   用户id
    //参数2：OrderConfirmDto orderConfirmDto:接收了确认订单请求参数的OrderConfirmDto对象
    //返回值：OrderConfirmVo：组装确认订单的出参
    public OrderConfirmVo orderConfirm(String userId, OrderConfirmDto orderConfirmDto);



    //提交订单的业务方法
    //参数1：userid
    //参数2：orderConfirmVo
    //返回值：订单号
    public  String orderSubmit(String userId, OrderConfirmVo orderConfirmVo);

    //消息回滚的方法     回查支付平台  确认未支付后执行的方法
    //参数1：订单对象
    //参数2：ChangeStock对象
    public void orderRollback(Order order, ChangeStock changeStock);
}
