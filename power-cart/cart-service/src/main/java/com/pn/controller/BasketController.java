package com.pn.controller;


import com.pn.entily.Basket;
import com.pn.entily.CartMoney;
import com.pn.entily.ShopCart;
import com.pn.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/p/shopCart")
@RestController
public class BasketController {


    //注入IBasketService
    @Autowired
    private BasketService basketService;
//-----------------------------------------------查询当前购物车商品数量----------------------------------------------------
    //url接口/p/shopCart/prodCount的请求处理方法
    @GetMapping("/prodCount")
    public ResponseEntity<Integer> getBasketCount() {
        //获取当前登录的用户名(我们存的是userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();
        //执行业务方法
        Integer count = basketService.findUserBasketCount(userId);

        return ResponseEntity.ok(count);
    }
//------------------------------修改当前购物车商品数量-----------------------------------------------
      /*
      url接口/p/shopCart/changeItem的请求处理方法;

      参数@RequestBody Basket basket用与接收小程序传递的购物车新增或修改的商品
      的信息的json数据;
     */
    @PostMapping("/changeItem")
    public ResponseEntity<Void>  changeItem(@RequestBody Basket basket){
        //获取当前登录的用户名(我们存的是userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();

        //给Basket对象设置userId
        basket.setUserId(userId);

        //执行业务方法
        basketService.changeItem(basket);

        return ResponseEntity.ok().build();

    }
    //--------------------------------------------查看当前用户购物车详情------------------------------------
    //url接口/p/shopCart/info的请求处理方法;
    @GetMapping("/info")
    public ResponseEntity<List<ShopCart>> getBasketInfo() {
        //获取当前登录的用户名(我们存的是userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();

        //执行业务方法
        List<ShopCart> shopCarts = basketService.getBasketInfo(userId);

        return ResponseEntity.ok(shopCarts);
    }
    //-----------------------------------删除商品的方法--------------------------------------------------------------
    /*
      url接口/p/shopCart/deleteItem的请求处理方法;
      参数@RequestBody List<Long> basketIds用于接收请求参数数组中被删除商品的id;
     */
    @DeleteMapping("/deleteItem")
    public ResponseEntity<Void> deleteItem(@RequestBody  List<Long> basketIds){
        //执行业务方法
        basketService.removeByIds(basketIds);
        return ResponseEntity.ok().build();
    }
//-----------------------------------计算商品总价的方法---------------------------------------------------------

    /*
      url接口/p/shopCart/totalPay的请求处理方法;
      参数@RequestBody List<Long> basketIds用于接口请求参数数组中的购物车中的所有商品id;
     */
    @PostMapping("/totalPay")
    public ResponseEntity<CartMoney> totalPay(@RequestBody List<Long> basketIds) {
        //执行业务方法
        CartMoney cartMoney = basketService.sumCartMoney(basketIds);
        return ResponseEntity.ok(cartMoney);
    }
/////-----------------------------------------从购物车确认商品的远程调用----------------------------------------------------
     /*
      url接口/p/shopCart/getBasketByIds的请求处理方法,根据购物车ids查询所有购物车;
      参数@RequestBody List<Long> basketIds用于接收请求参数购物车ids;
     */
    @PostMapping("/getBasketByIds")
    public List<Basket>getBasketByIds(@RequestBody List<Long> basketIds){
        return basketService.listByIds(basketIds);
    }
    //------------------------------------------远程调用接口（清空购物车的方法）------------------------------------------------------
     /*
      url接口/p/shopCart/deleteByUserIdAndSkuIds的请求处理方法,根据userId
      和skuIds从basket表中删除记录;
      参数@RequestParam String userId：用于接收请求参数userId;
      参数@RequestBody List<Long> skuIds：用于接收请求参数skuIds;
     */
    @RequestMapping("/deleteByUserIdAndSkuIds")
    public void deleteByUserIdAndSkuIds(@RequestParam String userId,@RequestBody List<Long> skuIds){
        //执行业务方法
        basketService.deleteByUserIdAndSkuIds(userId, skuIds);
    }
}
