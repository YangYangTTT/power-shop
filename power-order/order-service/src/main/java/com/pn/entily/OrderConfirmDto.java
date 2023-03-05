package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//此类为接收确认订单的入参的类，      分别为：接收商品处直接确认的入参     和接收购物车订单确认的入参
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderConfirmDto {

    //接收商品处直接确认订单的入参
    private OrderItem orderItem;

    //接收购物车确认订单的入参
    private List<Long> basketIds;
}
