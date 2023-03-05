package com.pn.service.Impi;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pn.CartProductFeign;
import com.pn.entily.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.BasketMapper;
import com.pn.service.BasketService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService{

    //注入BasketMapper
    @Autowired
    private BasketMapper basketMapper;
    //注入CartProductFeign  用于服务调用
    @Autowired
    private CartProductFeign cartProductFeign;

    //---------------------------------------------查询当前用户购物车商品数量-------------------------------------------------
    @Override
    public Integer findUserBasketCount(String userId) {
 /*
          从basket表中根据user_id列(用户id)查询记录行,然后计算查询到的
          行中basket_count列的和值,如果计算的和值为null则给0;
         */
        List<Object> list  = basketMapper.selectObjs(new QueryWrapper<Basket>()
                .select("ifnull(sum(basket_count),0)")
                .eq("user_id", userId)
        );
         /*
          如果查询的list集合不为空,取出0角标元素即为basket_count列的和值,
          然后将其转成整数返回;
         */
        if(!CollectionUtils.isEmpty(list)){
            Object obj =   list.get(0);
              return  Integer.parseInt(obj.toString());
        }
        return  0;

    }
//--------------------------------------------修改商品数量的方法--------------------------------------------------------------------
    //思路为先查询是否有商品  没有  直接添加   有 则将商品数量修改再更新
    @Override
    public void changeItem(Basket basket) {
        ///根据userid和sku_id查询是否有商品
        Basket basket1 = basketMapper.selectOne(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getUserId, basket.getUserId())
                .eq(Basket::getSkuId, basket.getSkuId())
        );

        //判断是有查询到商品    没有直接插入
        if(ObjectUtils.isEmpty(basket1)){
               basket.setBasketDate(new Date());
                basketMapper.insert(basket);
                return;
        }
        //有商品则修改商品数  然后更新
        int finalCount= basket1.getBasketCount()+ basket.getBasketCount();
        //如果finalcount为0则表示购物车中商品清空了
        if(finalCount==0){
            basketMapper.deleteById(basket1.getBasketId());
            return;
        }
        //如果和值大于0,则修改basket表中该商品记录的basket_count列的值为该和值
        basket1.setBasketDate(new Date());
        basket1.setBasketCount(finalCount);
        basketMapper.updateById(basket1);
    }
//-------------------------------------------------------查询购物车详情的方法-----------------------------------------------------------------
    @Override
    public List<ShopCart> getBasketInfo(String userId) {
        //先根据用户id  查询出所有商品
        List<Basket> baskets =  basketMapper.selectList(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getUserId,userId)
        );
        //判断 如果查询集合为空  则返回空集合
        if(CollectionUtils.isEmpty(baskets)){
            return Collections.emptyList();
        }
        //不为空  则通过所有商品中的商品id 查询锁有商品的sku
        List<Long>skuIds=new ArrayList<>();
        for(Basket basket:baskets){
            skuIds.add(basket.getSkuId());
        }
        //调用远程方法查询所有商品sku
        List<Sku> skuList  = cartProductFeign.getSkuByIds(skuIds);
          //如果list为空  则服务出错
        if(CollectionUtils.isEmpty(skuList)){
              throw new RuntimeException("服务器出错了");
        }
         /*
          组装数据返回
         */
        //创建店铺集合
        List<ShopCart> shopCarts = new ArrayList<>();
        //创建店铺对象
        ShopCart shopCart=new ShopCart();
        //创建商品条目集合
        List<CartItem> list=new ArrayList<>();
        //循环创建商品条目   并添加到商品条目集合
        for(Basket basket:baskets){
            for(Sku sku:skuList){
                if(basket.getSkuId().equals(sku.getSkuId())){
                    //创建商品条目
                    CartItem cartItem=new CartItem();
                    //将当次循环basket对象对应属性赋值给cartitem对象
                    BeanUtil.copyProperties(basket, cartItem, true);
                    //将当次循环sku对象对应的属性赋值给cartItem
                    BeanUtil.copyProperties(sku, cartItem, true);
                           //将cartItem对象 添加到cartIte集合
                    list.add(cartItem);
                }
            }
        }
        ////将条目集合设置进店铺对象
        shopCart.setShopCartItems(list);
        //将店铺对象 添加进店铺集合
        shopCarts.add(shopCart);
        //返回店铺集合
        return  shopCarts;
    }
//------------------------------------计算商品总金额的方法------------------------------------------
    @Override
    public CartMoney sumCartMoney(List<Long> basketIds) {
        ///判断参数  商品id是否为空

        if(StringUtils.isEmpty(basketIds)){
            return  null;
        }
        //根据参数查询出购物车中所有商品
        List<Basket> baskets = basketMapper.selectBatchIds(basketIds);
        //拿到所有商品的skuid
        List<Long> skuids=new ArrayList<>();
        for(Basket basket:baskets){
            skuids.add(basket.getSkuId());
        }
        //远程调用 查询所有商品skux信息
        List<Sku> skus = cartProductFeign.getSkuByIds(skuids);
        if(CollectionUtils.isEmpty(skus)){
            throw  new RuntimeException("服务器出错了");
        }
        //计算商品总价
        BigDecimal bigDecimal=new BigDecimal(0);
        for(Basket basket:baskets){
            for(Sku sku:skus){
                  if(basket.getSkuId().equals(sku.getSkuId())){
                      //价格×数量
                      BigDecimal money = sku.getPrice().multiply(new BigDecimal(basket.getBasketCount()));
                        bigDecimal.add(money);
                  }
            }

        }
        //组装数据
        CartMoney cartMoney=new CartMoney();
        cartMoney.setTotalMoney(bigDecimal);
        return cartMoney;
    }

    //////--------------------------------------------------清空购物车的方法--------------------------------------------------------------------
    @Override
    public void deleteByUserIdAndSkuIds(String userId, List<Long> skuIds) {
        //
        //delete from basket where user_id = ? and sku_id in(?,?,?...)
      basketMapper.delete(new LambdaQueryWrapper<Basket>()
              .eq(Basket::getUserId,userId)
              .eq(Basket::getSkuId,skuIds)
      );
    }
}
