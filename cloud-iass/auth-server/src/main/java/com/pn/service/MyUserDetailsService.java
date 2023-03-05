package com.pn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.pn.config.AuthConstant;


import com.pn.dao.SysLoginUserMapper;
import com.pn.dao.WxUserMapper;
import com.pn.entily.SysLoginUser;
import com.pn.entily.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

////注入mapper
@Autowired
private SysLoginUserMapper userMapper;
    //获取配置文件的wx.appid属性值,即微信小程序的第三方标识id,赋值给属性appId
    @Value("${wx.appid}")
    private String appId;

    //获取配置文件的wx.appsecret属性值,即微信小程序的第三方密码,赋值给属性appsecret
    @Value("${wx.appsecret}")
    private String appSecret;

    //获取配置文件的wx.tokenurl属性值,即向微信发送认证请求的url,赋值给属性tokenUrl
    @Value("${wx.tokenurl}")
    private String tokenUrl;

    //注入RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    //注入WxUserMapper
    @Autowired
    private WxUserMapper wxUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ////先获取请求头  logintype的值  判断  是 前台用户登陆还是后台用户登陆
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginType = request.getHeader(AuthConstant.LOGIN_TYPE);
       if(StringUtils.isEmpty(loginType)){
           return null;
       }
    switch(loginType){
           //后台用户  -- vue项目的登录
        case AuthConstant.SYS_USER:
            //根据username  查询 用户信息--- select * from sys_user where username = ? and status = 1;
            SysLoginUser loginUser = userMapper.selectOne(
                    new LambdaQueryWrapper<SysLoginUser>()
                            .eq(SysLoginUser::getUsername, username)
                            .eq(SysLoginUser::getStatus, 1)
            );
            //如果用户不为空  则查询权限
            if(!ObjectUtils.isEmpty(loginUser)){
                List<String> authorities = userMapper.queryAuthorities(loginUser.getUserId());
                if(!CollectionUtils.isEmpty(authorities)){
                    loginUser.setAuthorities(authorities);
                }
            }
            return loginUser;
            //如果请求头loginType值为user, 前台系统用户登录 --- 微信小程序的登录
        case AuthConstant.USER:
            //替换向微信发送认证请求的url地址中的占位符 --code码即为请求参数username
            //string.format方法为将第一个参数中的占位符替换成  后面参数
            tokenUrl=String.format(tokenUrl,appId,appSecret,username);
            //向微信发送认证请求
            String wxResponse =restTemplate.getForObject(tokenUrl,String.class);
            //认证通过后，微信返回的是一个json串，解析json串拿到openid
            JSONObject jsonObj =JSON.parseObject(wxResponse);
           //拿到openid
            String openid = jsonObj.getString("openid");
            System.out.println(openid);

            //判断openid为不为空
            if(StringUtils.hasText(openid)){
                //根据openid即user表中的user_id列从表中查询用户
                WxUser wxUser =    wxUserMapper.selectById(openid);
                //如果查询的用户不为空  则说明用户使用微信小程序已经登陆过系统
                if(!ObjectUtils.isEmpty(wxUser)){
                      //直接返回查询到的用户
                    return  wxUser;
                }
                ///如果查询到的用户为空，说明用户使用微信小程序第一次登陆系统
                //则向user表中插入用户信息进行注册，并返回插入的用户
                wxUser  = createUser(openid);
                return wxUser;
            }
    }
   return null;
    }

    private WxUser  createUser(String openid) {
          //获取客户端ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip=request.getRemoteAddr();

        WxUser wxUser = new WxUser();
        //设置user_id列为openid
        wxUser.setUserId(openid);
        //设置用户注册时间为系统当前时间
        wxUser.setUserRegtime(new Date());
        //设置用户最后登录时间为系统当前时间
        wxUser.setUserLasttime(new Date());
        //设置用户修改时间为系统当前时间
        wxUser.setModifyTime(new Date());
        //设置用户注册ip
        wxUser.setUserRegip(ip);
        //设置用户最后登录ip
        wxUser.setUserLastip(ip);
        //设置状态为1正常
        wxUser.setStatus(1);
        //添加用户
        wxUserMapper.insert(wxUser);
        //返回用户
        return wxUser;
    }
}
