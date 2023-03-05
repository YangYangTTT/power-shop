package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.entily.UserAddr;
import com.pn.service.UserAddrService;
import com.pn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/p/address")
@RestController
public class UserAddrController {

    //注入UserAddService
    @Autowired
    private UserAddrService userAddrService;


    ///------------------------------------------------根据用户id查询用户所有收货地址-------------------------------------
    //url接口/p/address/list的请求处理方法
    @GetMapping("/list")
        public ResponseEntity<List<UserAddr>> getUserAddr(){
         //拿到当前登录用户id
        String userid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<UserAddr> userAddr = userAddrService.findUserAddr(userid);
        return ResponseEntity.ok(userAddr);
    }
////-------------------------------------------------------修改用户默认收货地址------------------------------------------------
    @PutMapping("/defaultAddr/{addrId}")
    public  ResponseEntity<Void>defaultAddr(@PathVariable("addrId")    Long addrId){
        //拿到当前登录的用户名(我们存的是userId)
        String userId  = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userAddrService.defaultAddr(addrId, userId);
        return  ResponseEntity.ok().build();
    }
    //--------------------------------------------------新增收获地址-------------------------------------------
    @PostMapping("/addAddr")
    public ResponseEntity<Void> addUserAddr(@RequestBody UserAddr userAddr) {
        //拿到当前登录的用户名(我们存的是userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();
        //参数userAddr对象设置userId
        userAddr.setUserId(userId);

        //执行业务方法
        userAddrService.save(userAddr);

        return ResponseEntity.ok().build();
    }
    //------------------------------------供order-service服务调用的方法-----------------------------------------------------------
     /*
          从user_addr表中根据user_id列(用户id)查询status列值为1(状态正常)
          common_addr列值也为1(默认地址)的记录行;
         */
    @GetMapping("/userDefaultAddr")
    public UserAddr getUserDefaultAddr(@RequestParam("userId") String userId) {

        return  userAddrService.getOne(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userId)
                .eq(UserAddr::getCommonAddr, 1)
                .eq(UserAddr::getStatus, 1)
        );
    }
}
