package com.pn.service;

import com.pn.entily.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pn.entily.CartMoney;
import com.pn.entily.ShopCart;

import java.util.List;

public interface BasketService extends IService<Basket>{
//查询当前购物车的商品数量
public Integer findUserBasketCount(String userId);

//修改商品数量的方法
public void changeItem(Basket basket);

//查询购物车详情的方法
      public List<ShopCart> getBasketInfo(String userId);
      //计算商品总金额的方法
        public CartMoney sumCartMoney(List<Long> basketIds);


        //清空购物车的方法(在order-service远程调用   )
    public  void deleteByUserIdAndSkuIds(String userId, List<Long> skuIds);
}
