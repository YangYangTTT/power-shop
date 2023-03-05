package com.pn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {
    //注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    //登出接口
    @RequestMapping("/sys/logout")
  public ResponseEntity<Void> logout(){
        //得到当前用户id
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)  RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String authorization = request.getHeader("Authorization");
        //去除前缀 -- 真正的token
        String authorization1 = authorization.replaceAll("Authorization", "");
        //从redis删除当前登录用户的token
        redisTemplate.delete(authorization1);

           //响应
        return ResponseEntity.ok().build();
        ////或者给参数HttpServletRequest request   直接request.getheader  比较方便
    }
}
