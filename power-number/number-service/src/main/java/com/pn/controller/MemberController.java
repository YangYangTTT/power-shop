package com.pn.controller;


import com.pn.entily.User;
import com.pn.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/p/user")
@RestController
public class MemberController {

    //注入IUserService
    @Autowired
    private UserService userService;

    /*
      put请求方式的url接口/p/user/setUserInfo的请求处理方法:
      参数@RequestBody User user封装小程序发送的当前微信登录用户信息的json数据;
     */
    @PutMapping("/setUserInfo")
    public ResponseEntity<Void> updateUserInfo(@RequestBody User user) {
        //获取当前登陆的用户名（openid  即userid）
        String userId =SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //将userId存储到参数User对象
        user.setUserId(userId);
        //执行更新
        userService.updateById(user);
        return  ResponseEntity.ok().build();
    }
}
