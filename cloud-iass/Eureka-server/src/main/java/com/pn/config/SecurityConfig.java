package com.pn.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //取消跨域 跨站请求的检查
        http.cors().disable();//域
        http.csrf().disable();//站点
        ////actuator所有的监控端点（url）都放行
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        ////调用父类的configure(Httpsecurity),提供登陆表单 ，且认证通过给请求放行
super.configure(http);
    }
}
