package com.pn.controller;


import com.pn.entily.OrderConfirmDto;
import com.pn.entily.OrderStatusCount;
import com.pn.service.OrderService;
import com.pn.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/p/myOrder")
@RestController
public class OrderController {

    //注入orderservice
    @Autowired
    private OrderService orderService;

//--------------------------------------------查询当前用户所有订单    各个状态数量--------------------------------------------------
@GetMapping("/orderCount")
    public ResponseEntity<OrderStatusCount>  getUserOrderStatusCount(){
                //获取当前用户id
    String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
      //执行业务方法
    OrderStatusCount orderStatusCount = orderService.selectStatusCount(userId);
    return  ResponseEntity.ok(orderStatusCount);
}
//////-----------------------------------------确认商品方法-------------------------------------------------------
    //用userid获取默认地址   直接存在OrderConfirmVo中
      /*
      url接口/p/myOrder/confirm的请求处理方法--确认订单请求;
      参数@RequestBody OrderConfirmDto orderConfirmDto用于接收确认订单请求传递的请求参数;
     */
    @PostMapping("/confirm")
    public ResponseEntity<OrderConfirmVo>orderConfirm(@RequestBody OrderConfirmDto orderConfirmDto){
        //获取当前登录的用户名(我们存的是userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();
        //执行业务方法
        OrderConfirmVo orderConfirmVo =
                orderService.orderConfirm(userId, orderConfirmDto);

        return ResponseEntity.ok(orderConfirmVo);
    }
    //-------------------------------------提交订单的方法--------------------------------------------------------------------
     /*
      url接口/p/myOrder/submit的请求处理方法--提交订单请求;
      参数@RequestBody OrderConfirmVo orderConfirmVo用于接收提交订单请求传递的请求参数;
     */
    @PostMapping("/submit")
    public ResponseEntity<String>  orderSubmit(@RequestBody OrderConfirmVo orderConfirmVo){
        //获取当前登陆用户名即userid
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //执行业务方法
        String orderNum =orderService.orderSubmit(userId, orderConfirmVo);

        //返回给前端的订单号是全数字会丢失进度,所以加个前缀orderNum
        return ResponseEntity.ok("orderNum:"+orderNum);
    }
}
