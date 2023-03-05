package com.pn.service.impi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.User;
import com.pn.dao.UserMapper;
import com.pn.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{


    //注入userMapper
    @Autowired
    private  UserMapper userMapper;


    //重写updateById()方法


    @Override
    public boolean updateById(User user) {
         ///获取客户端ip
       ServletRequestAttributes servletRequestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = request.getRemoteAddr();

        //设置用户最后登录ip
        user.setUserLastip(ip);
        //设置用户修改时间为系统当前时间
        user.setModifyTime(new Date());
        //设置用户最后登录时间为系统当前时间
        user.setUserLasttime(new Date());
        //如果性别为1则赋值为M,为0则赋值为F
        user.setSex(user.getSex().equals("1")? "M":"F");
        //执行更新
        return userMapper.updateById(user) > 0;

    }
}
