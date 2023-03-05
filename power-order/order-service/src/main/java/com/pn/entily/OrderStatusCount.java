package com.pn.entily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//查询当前用户所有订单的各个状态数量  所要用到的类
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderStatusCount {

    //待支付
    private  Integer unpay=0;
    //代发货
    private  Integer  payed=0;
    //待收货
    private  Integer consignment=0;
}
