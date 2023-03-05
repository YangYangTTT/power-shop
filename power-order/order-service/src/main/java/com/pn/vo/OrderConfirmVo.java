package com.pn.vo;


import com.pn.entily.ShopOrder;
import com.pn.entily.UserAddr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

//组装确认订单的出参
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmVo {
    //user_addr表的实体类UserAddr对象封装默认收货地址
    private UserAddr userAddr;

    //订单中的所有店铺--一个店铺有多个商品--即所有商品信息
    private List<ShopOrder> shopCartOrders;

    //商品总数量
    private Integer totalCount;


    //商品总金额
    private BigDecimal total;

    //商品实际总金额
    private BigDecimal actualTotal;

    //接收提交订单时的买家留言
    private String remarks;
}
