package com.pn.config;
//////这里面配置   service查询权限的方法    得到登陆人员的权限


import com.pn.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //注入查询权限的service
    @Autowired
    private MyUserDetailsService myUserDetailsService;

/////////////////1:认证   重写 auth调用userDetailservice方法  将注入的service类作为参数
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    ////////////2:http请求配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ///取消跨站请求
        http.csrf().disable();
       ////取消跨域请求
        http.cors().disable();
        /////对actuator的所有监控端点进行 放行
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
   /////其他资源必须认证通过后才可以访问
        http.authorizeRequests().anyRequest().authenticated();
    }


    //////////3:配置认证管理器   给密码模式的授权使用  实现隐式登陆
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
