package com.pn.entily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//////此类为店铺list   里面存放  此店铺所有商品   CartItem
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCart {

    //该店铺在购物车中的商品条目的集合
    private List<CartItem> shopCartItems;
}
