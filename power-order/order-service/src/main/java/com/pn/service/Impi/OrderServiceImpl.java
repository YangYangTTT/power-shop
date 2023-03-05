package com.pn.service.Impi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.OpenFign.OrderCartFeign;
import com.pn.OpenFign.OrderProductFeign;
import com.pn.OpenFign.OrderUserAddrFeign;
import com.pn.dao.EsProdDao;
import com.pn.entily.*;
import com.pn.service.OrderItemService;
import com.pn.service.OrderService;
import com.pn.vo.OrderConfirmVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.OrderMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    //注入OrderMapper
    @Autowired
    private  OrderMapper orderMapper;
    ///注入snowflake
    @Autowired
    private Snowflake snowflake;
    //注入esprodDao
    @Autowired
    private EsProdDao esProdDao;
    //注入orderitemService
    @Autowired
    private OrderItemService  orderItemService ;
    //注入rabbitmq
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //---------------------------------查询用户所有订单的各个状态的方法-------------------------------------------
    @Override
    public OrderStatusCount selectStatusCount(String userId) {
        ///先根据用户id查询出此用户所有的订单
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
        );
        /////将订单中的属性值一一对比组装进OrderStatusCount类中返回
        OrderStatusCount orderStatusCount=new OrderStatusCount();
        if(!CollectionUtils.isEmpty(orders)){
             int unpay=0;
             int payed=0;
             int consignment=0;
                for(Order order:orders){
                    if(order.getStatus().equals(1)){
                        unpay++;
                    }else if(order.getStatus().equals(2)){
                        payed++;
                    }else if(order.getStatus().equals(3)){
                        consignment++;
                    }
                }
            orderStatusCount.setUnpay(unpay);
                orderStatusCount.setPayed(payed);
                orderStatusCount.setConsignment(consignment);
        }
        return orderStatusCount;
    }
///--------------------------------------------确认订单的方法------------------------------------------------------------------------------
    //注入OrderProductFeign
    @Autowired
    private OrderProductFeign orderProductFeign;

    //注入OrderUserAddrFeign
    @Autowired
    private OrderUserAddrFeign orderUserAddrFeign;

    //注入OrderCartFeign
    @Autowired
    private OrderCartFeign orderCartFeign;
    @Override
    public OrderConfirmVo orderConfirm(String userId, OrderConfirmDto orderConfirmDto) {
        OrderConfirmVo orderConfirmVo=new OrderConfirmVo();
              /*
          远程调用member-service服务的url接口/p/address/userDefaultAddr
          查询用户的默认收货地址
         */
        //得到默认收货地址
        UserAddr userDefaultAddr = orderUserAddrFeign.getUserDefaultAddr(userId);
        //向OrderConfirmVo对象组装默认收货地址
        orderConfirmVo.setUserAddr(userDefaultAddr);
            /*
          拿到参数OrderConfirmDto对象接收的存放了所有购物车id的List<Long>属性
          并判断,如果List<Long>为空说明是从商品处直接确认订单,如果List<Long>不
          为空说明是从购物车确认订单;
         */
        List<Long> basketIds = orderConfirmDto.getBasketIds();
        if(CollectionUtils.isEmpty(basketIds)){
            //2：从商品处直接确认订单的处理
            shoppingCart(orderConfirmDto.getOrderItem(),orderConfirmVo);
        } else{
            //1：从购物车确认订单的处理
            cartToConfirm(basketIds, orderConfirmVo);
        }
        return orderConfirmVo;
    }


    //商品直接确认
    private void  shoppingCart(OrderItem orderItem, OrderConfirmVo orderConfirmVo) {
                        //从orderitem中拿到skuid    通过远程调用 根据skuid查询sku
        Long skuId = orderItem.getSkuId();
        //远程调用
        List<Sku> skuByIds = orderProductFeign.getSkuByIds(Arrays.asList(skuId));
        if(CollectionUtils.isEmpty(skuByIds)){
            throw  new RuntimeException("商品直接确认远程调用getSkuByIds出错");
        }
        //拿到商品sku
        Sku sku = skuByIds.get(0);
        //计算商品总金额
        BigDecimal totalPrice = sku.getPrice().multiply(new BigDecimal(orderItem.getProdCount()));
        //向OrderConfirmVo对象组装商品总金额
        orderConfirmVo.setTotal(totalPrice);
        //向OrderConfirmVo对象组装商品实际总金额
        orderConfirmVo.setActualTotal(totalPrice);
        //向OrderConfirmVo对象组装商品总数量
          orderConfirmVo.setTotalCount(orderItem.getProdCount());
       // 向OrderConfirmVo对象组装订单中的所有店铺,即所有商品信息:
        //创建店铺集合
        List<ShopOrder> shopOrders=new ArrayList<>();
        //创建店铺
        ShopOrder shopOrder=new ShopOrder();
        //创建商品条目集合
        List<OrderItem> orderItems=new ArrayList<>();
        //将Sku对象的对应属性值赋值给,接收请求参数的商品条目OrderItem对象的对应属性
        BeanUtil.copyProperties(sku, orderItem, true);
        //给商品条目设置商品总价   上面计算的totalPrice
        orderItem.setProductTotalAmount(totalPrice);
        //将商品条目OrderItem对象添加到商品条目List集合中
        orderItems.add(orderItem);
        //将商品条目List集合添加到店铺中
        shopOrder.setShopCartItemDiscounts(orderItems);
        //将店铺添加到店铺List集合中
        shopOrders.add(shopOrder);
        //向OrderConfirmVo对象组装订单中的所有店铺
        orderConfirmVo.setShopCartOrders(shopOrders);
    }

    //购物车确认
    public void  cartToConfirm(List<Long> basketIds, OrderConfirmVo orderConfirmVo){

           /*
          远程调用cart-service服务的url接口/p/shopCart/getBasketByIds,
          根据购物车ids查询所有购物车
         */
        List<Basket> basketList  = orderCartFeign.getBasketByIds(basketIds);
        if(CollectionUtils.isEmpty(basketList)){
            throw new RuntimeException("调用orderCartFeign查询所有所有购物车失败");
        }
        //拿到每个购物车的skuid  保存到lsist集合
        List<Long> skuIds = new ArrayList<>();
        for(Basket basket:basketList){
            skuIds.add(basket.getSkuId());
        }
         /*
          远程调用product-service服务的url接口/prod/prod/getSkuByIds
          根据skuids查询所有SKU;
         */
        List<Sku> skuList  = orderProductFeign.getSkuByIds(skuIds);
        if(CollectionUtils.isEmpty(skuList)){
            throw  new RuntimeException("调用orderProductFeign查询所有sku失败");
        }
        //存放购物车中每个商品的总金额的List<BigDecimal>集合
        List<BigDecimal> totalMoneyList = new ArrayList<>();
        //存放购物车中每个商品的商品条目的List<OrderItem>集合
        List<OrderItem> orderItems = new ArrayList<>();
        //循环处理购物车中的每个商品
        for(Basket basket:basketList){
            //每个basket的sku
            for(Sku sku:skuList){
                if(basket.getSkuId().equals(sku.getSkuId())){
                    //计算当前商品的总金额
                    BigDecimal oneTotalMoney  = sku.getPrice().multiply(new BigDecimal(basket.getBasketCount()));
                    //将当前商品总金额加入,存放购物车中每个商品的总金额的List<BigDecimal>集合
                    totalMoneyList.add(oneTotalMoney);
                    //为当前商品创建商品条目OrderItem对象
                    OrderItem orderItem = new OrderItem();
                    //将当前购物车Basket对象的对应属性值赋值给,当前商品的商品条目OrderItem对象的对应属性
                    BeanUtil.copyProperties(basket, orderItem, true);
                    //将当前Sku对象的对应属性值赋值给,当前商品的商品条目OrderItem对象的对应属性
                    BeanUtil.copyProperties(sku,orderItem,true);
                    //给当前商品的商品条目OrderItem对象设置当前商品的总金额
                    orderItem.setProductTotalAmount(oneTotalMoney);
                    //给当前商品的商品条目OrderItem对象设置当前商品的总数量
                    orderItem.setProdCount(basket.getBasketCount());
                    //将当前商品的商品条目OrderItem对象加入,存放购物车中每个商品的商品条目的List<OrderItem>集合
                    orderItems.add(orderItem);
                }
            }
        }
        //计算购物车中所有商品总金额
        BigDecimal totalMoney =new BigDecimal(0);
        //将存放每个商品总金额的list遍历  计算出所有商品总金额
        for(BigDecimal oneTotalMoney:totalMoneyList){
            totalMoney=totalMoney.add(oneTotalMoney);
        }

        //计算购物车中所有商品的总数量
        Integer totalCount =0;
        for(Basket basket:basketList){
          totalCount+=basket.getBasketCount();
        }
        //向OrderConfirmVo对象组装商品总数量
        orderConfirmVo.setTotalCount(totalCount);
        //向OrderConfirmVo对象组装商品总金额
        orderConfirmVo.setTotal(totalMoney);
        //向OrderConfirmVo对象组装商品实际总金额
        orderConfirmVo.setActualTotal(totalMoney);
           /*
          向OrderConfirmVo对象组装订单中的所有店铺,即所有商品信息:
         */
        //创建店铺List集合
        List<ShopOrder> shopOrders = new ArrayList<>();
        //创建店铺--我们只有一个店铺
        ShopOrder shopOrder = new ShopOrder();
        //将商品条目List集合添加到店铺中
        shopOrder.setShopCartItemDiscounts(orderItems);
        //将店铺添加到店铺List集合中
        shopOrders.add(shopOrder);
        //向OrderConfirmVo对象组装订单中的所有店铺
        orderConfirmVo.setShopCartOrders(shopOrders);
    }
///--------------------------------------------提交订单的方法-----------------------------------------------------------------------------------
    @Override
    public String orderSubmit(String userId, OrderConfirmVo orderConfirmVo) {
        //1.生成订单号
        String orderNum = createOrderNum();

        //2.清空购物车(方法在cart-service BasketService中   然后再下面的clearCart方法中远程调用 )
        clearCart(userId, orderConfirmVo);

        //3.减mysql的库存
        ChangeStock changeStock = changeDbStock(orderConfirmVo);

        //4.减少es库存的方法
        changeEsStock(changeStock);

        //5.写订单表
        BigDecimal money = writeOrder(userId,orderNum,changeStock,orderConfirmVo);

        //6:写延迟队列
        writeMsQueue(orderNum, changeStock);

        //返回订单号
        return orderNum;
    }




    //**********************1:生成订单号的方法
    private String createOrderNum() {
          //生成订单号并返回
        return  snowflake.nextIdStr();
    }

    //*********************2:清除购物车的方法
    //在商品页下单与在购物车下单是两种不同的情况   只有在购物车下单才需清空购物车  此处一视同仁  都需清空
    private void clearCart(String userId, OrderConfirmVo orderConfirmVo) {
          /*
          拿到skuIds（用userid和skuids作为删除购物车的条件）
         */
        //拿到所有店铺
        List<ShopOrder> shopCartOrders = orderConfirmVo.getShopCartOrders();
        //拿到每个店铺的所有商品条目,再拿到每个商品条目对应的skuid,存储到List<Long>集合中
        List<Long>skuIds=new ArrayList<>();
        for(ShopOrder shopOrder:shopCartOrders){
            //拿到所有商品条目
            List<CartItem> shopCartItemDiscounts = shopOrder.getShopCartItemDiscounts();
            for(CartItem orderItem:shopCartItemDiscounts){
                //将商品条目中的skuid  放进存放skuid的list中
                skuIds.add(orderItem.getSkuId());
            }
        }
        //远程调用cart-service服务的url接口/p/shopCart/deleteByUserIdAndSkuIds,
        //根据userId和skuIds从basket表中删除记录
        orderCartFeign.deleteByUserIdAndSkuIds(userId,skuIds);
    }

    //3********************************:减少mysql库存的方法
    private ChangeStock changeDbStock(OrderConfirmVo orderConfirmVo) {
        //创建个List<ProdChange>集合,组装所有的ProdChange
        List<ProdChange> prodChanges = new ArrayList<>();
        //创建个List<SkuChange>集合,组装所有的SkuChange
        List<SkuChange> skuChanges = new ArrayList<>();
        //拿到每个店铺的信息
        List<ShopOrder> shopCartOrders = orderConfirmVo.getShopCartOrders();
        for(ShopOrder shopOrder:shopCartOrders){
            //拿到OrderItem的集合
            List<CartItem> OrderItems = shopOrder.getShopCartItemDiscounts();
            for(CartItem orderItem:OrderItems){
                SkuChange skuChange=new SkuChange();
                Long skuId = orderItem.getSkuId();
                //乘负一的目的在于  执行sql语句时可以直接加count  效果等于减去count
                Integer prodCount = orderItem.getProdCount()*-1;
                skuChange.setSkuId(skuId);
                skuChange.setCount(prodCount);
                skuChanges.add(skuChange);
                //拿到prodId
                Long prodId = orderItem.getProdId();
                //判断 List<ProdChange> prodChanges大小
                int size = prodChanges.size();
                if(size==0){
                    //如果size为0说明集合为空   知己设置prodid以及count即可
                    ProdChange prodChange=new ProdChange();
                    prodChange.setProdId(prodId);
                    prodChange.setCount(prodCount);
                }else{//size不为零  则判断list里面的prodid与orderItem中要往prodChange中添加的prodid是否相同
                    for(int i=0;i<size;i++){
                        //1：如果原本集合里面的数据与要添加的相等   则将当前集合中的数量减去要添加的数量
                        if(prodChanges.get(i).getProdId().equals(prodId)){//集合中东西与要添加东西一样  只修改库存  id原本就有
                            //相当于我现在有5   要再减去相同的5   所以直接set5-5即可
                            prodChanges.get(i).setCount(prodChanges.get(i).getCount()+prodCount);
                        }else{//集合里面的东西和要添加的不一致   则正常将prodid和数量设置进去即可
                            ProdChange prodChange=new ProdChange();
                            prodChange.setProdId(prodId);
                            prodChange.setCount(prodCount);
                            prodChanges.add(prodChange);
                        }
                    }


                }
            }
        }
        //将List<ProdChange>和List<SkuChange>组装到ChangeStock中
        ChangeStock changeStock=new ChangeStock(prodChanges,skuChanges);
        //远程调用product-service服务的url接口/prod/prod/updateStock,
        //修改sku表和prod表的库存;
        orderProductFeign.updateStock(changeStock);
        return  changeStock;
    }

    //**********************************4:减少es库存的方法
    private void changeEsStock(ChangeStock changeStock) {
        //思路为从参数中拿到商品id  从es中查到这些商品   修改完数量以后 再保存
/*
 ES中只需要减去商品的库存,所以拿到减mysql库存的方法返回的ChangeStock对象
 中,存储了所有要减去库存的商品id和数量的ProdChange对象的List<ProdChange>
 集合;
*/
        List<ProdChange> prodChanges = changeStock.getProdChanges();
        ////拿到商品的ids
        List<Long>prodIds=new ArrayList<>();
        for(ProdChange prodChange:prodChanges){
            prodIds.add(prodChange.getProdId());
        }
        //根据商品id从es中查询出所有商品
        Iterable<EsProd> esProds  = esProdDao.findAllById(prodIds);
        //遍历商品  修改数量
        for(EsProd esProd:esProds ){
                for(ProdChange prodChange:prodChanges){
                    if(esProd.getProdId().equals(prodChange.getProdId())){
                        //计算当前商品减去后的库存数量(商品现有库存+ProdChange里面要减去的库存（是负数所以加）)
                        long newTotalStock =esProd.getTotalStocks()+prodChange.getCount();
                         if(newTotalStock<0){
                             throw  new RuntimeException("es减库存时 库存不足");
                         }
                         //修改当前 商品库存
                        esProd.setTotalStocks(newTotalStock);
                         //修改当前商品销量
                        esProd.setSoldNum(esProd.getSoldNum()-prodChange.getCount());
                    }

                }
        }
        //重新保存所有商品，即修改所有商品库存和销量
        esProdDao.saveAll(esProds);
    }

    //5:*************************************写订单的方法
    private BigDecimal writeOrder(String userId, String orderNum, ChangeStock changeStock, OrderConfirmVo orderConfirmVo) {
        //写订单的步骤为  先从ChangeStock对象中拿到getSkuChanges再拿到所有skuid放在集合中  然后远程调用方法查询所有
        //sku  然后双循环   将sku内容copy进新建的orderItem中   然后再设置orderItem中各个属性  将orderItem添加到orderItemList中
        //执行添加方法  添加orderItemList    判断是否添加成功   成功的话向order对象中设置各个属性  然后执行方法添加order订单
        ///然后返回商品总金额
        List<SkuChange> skuChanges = changeStock.getSkuChanges();
        List<Long> skuIds=new ArrayList<>();
        for(SkuChange skuChange:skuChanges){
            skuIds.add(skuChange.getSkuId());
        }
        //远程据skuid查询所有sku调用  根
        List<Sku> skuList  = orderProductFeign.getSkuByIds(skuIds);
        if(CollectionUtils.isEmpty(skuList)){
            throw  new RuntimeException("写订单时远程调用 getSkuByIds方法失败 ");
        }
        List<OrderItem> orderItemList=new ArrayList<>();
        BigDecimal allTotalMoney  = new BigDecimal(0);
        //商品总数
        int allCount = 0;
        for(SkuChange skuChange:skuChanges){
            for(Sku sku:skuList){
                if(skuChange.getSkuId().equals(sku.getSkuId())){
                    //创建订单条目OrderItem对象
                    OrderItem orderItem=new OrderItem();
                    //将当前Sku的对应属性值赋值给OrderItem对象对应属性
                    BeanUtil.copyProperties(sku,orderItem,true);
                    //设置当前订单条目所属的订单号
                    orderItem.setOrderNumber(orderNum);
                    //设置当前订单条目的单品数量(乘负一是因为里面存的本来就是负数)
                   orderItem.setProdCount((skuChange.getCount()*-1));
                    //设置当前订单条目的单品总金额
                    BigDecimal oneTotalMoney =sku.getPrice().multiply(new BigDecimal((skuChange.getCount()*-1)));
                    //设置shop_id为1
                    orderItem.setShopId(1L);
                    //设置创建时间为系统当前时间
                    orderItem.setRecTime(new Date());
                    //将当前OrderItem加入List<OrderItem>集合
                    orderItemList.add(orderItem);
                    //累加所有商品总金额
                    allTotalMoney =  allTotalMoney.add(oneTotalMoney);
                    //累加所有商品总数量
                    allCount+=(skuChange.getCount()*-1);
                }
            }
        }
        //向order_item表中批量添加订单条目
        boolean bool =   orderItemService.saveBatch(orderItemList);
        //判断添加商品条目是否成功  成功则向order表中添加订单
        /*
          向orders表中添加订单
         */
        if(bool){
            //创建Order对象
            Order order = new Order();
            //设置订单号
            order.setOrderNumber(orderNum);
            //设置订单所属userId
            order.setUserId(userId);
            //设置订单总金额和实际总金额
            order.setTotal(allTotalMoney);
            order.setActualTotal(allTotalMoney);
            //设置订单商品总数量
            order.setProductNums(allCount);
            //设置订单收货地址id
            order.setAddrOrderId(orderConfirmVo.getUserAddr().getAddrId());
            //设置订单的买家留言
            order.setRemarks(orderConfirmVo.getRemarks());
            //设置订单状态为1--待支付
            order.setStatus(1);
            //设置订单创建时间为系统当前时间
            order.setCreateTime(new Date());
            //设置is_payed为0未支付
            order.setIsPayed(0);
            //设置shop_id为1
            order.setShopId(1L);
            //设置delete_status为0未删除
            order.setDeleteStatus(0);
      //添加订单
            orderMapper.insert(order);
        }
        //返回所有商品金额
        return  allTotalMoney;
    }
    //6:************************写延迟队列的方法-----------------------------------------------------------------------
    private void writeMsQueue(String orderNum, ChangeStock changeStock) {
        //将订单号和减mysql库存的方法返回的ChangeStock对象,转成json串作为消息
        Map<String, Object> jsonObj = new HashMap<>();
        jsonObj.put("orderNum", orderNum);
        jsonObj.put("changeStock", changeStock);
        String jsonStr = JSON.toJSONString(jsonObj);
        //向延迟队列发送消息
        rabbitTemplate.convertAndSend(QueueConstant.ORDER_MS_QUEUE,jsonStr);
    }

    ///-----------------------------------回查支付平台 确定为支付时执行的消息回滚方法--------------------------------------------
    @Transactional
    @Override
    public void orderRollback(Order order, ChangeStock changeStock) {
   ///将订单状态改为6
        order.setStatus(6);
        order.setUpdateTime(new Date());
        order.setDeleteStatus(2);
        //执行方法  修改以上信息
        int i = orderMapper.updateById(order);
        //修改完成以后   判断是否修改成功  成功分别将数据库  以及es数据库中的库存改为原数
        if(i>0){
            List<ProdChange> prodChanges = changeStock.getProdChanges();
            for(ProdChange prodChange:prodChanges){
                ///原本的要减库存的数时负数 应为mapper执行方法时时加   相当于减库存
                //此处设置为正数   相当于加上要减的数量  相当于 回滚 恢复到未支付的状态
                    prodChange.setCount((prodChange.getCount()*-1));
            }
            List<SkuChange> skuChanges = changeStock.getSkuChanges();
            for(SkuChange skuChange:skuChanges){
                skuChange.setCount((skuChange.getCount()*-1));
            }
                 ///执行updateStock  方法  相当于回滚(mysql)
              orderProductFeign.updateStock(changeStock);
            //回滚es中商品数量（es）
            changeEsStock(changeStock);
        }
    }
}
