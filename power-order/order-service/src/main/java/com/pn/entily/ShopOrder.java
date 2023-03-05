package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrder {

//订单中的商品属于不同店铺，每个店铺有多个商品
    //店铺中的所有商品条目
    private List<CartItem> shopCartItemDiscounts;
}
