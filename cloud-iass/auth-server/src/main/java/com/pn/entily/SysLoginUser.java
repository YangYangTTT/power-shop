package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * 系统用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysLoginUser implements Serializable, UserDetails {

    //用户id
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    //用户名
    @TableField(value = "username")
    private String username;

    //密码
    @TableField(value = "password")
    private String password;


    //状态  0：禁用   1：正常
    @TableField(value = "status")
    private Integer status;

    //存储用户所有字符串形式的权限的List<String>集合属性
    @TableField(exist = false)
    private List<String> authorities;

    //-------------------------------------------------

    //UserDetails返回用户权限的方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
       //创建存储权限的集合
        List<GrantedAuthority>authorityList=new ArrayList<>();
    for(String authority:authorities){
           if(StringUtils.hasText(authority)){//判断是否为空
               if(authority.contains(",")){//判断是否包含逗号，包含的话证明有多个权限
                  String[]authorityArr=authority.split(",");//将权限  以逗号为分界 分别放在集合中
                     for(String auth:authorityArr){  //循环权限
                         SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(auth);
                         authorityList.add(simpleGrantedAuthority);
                     }
               }else{//单个权限
                   SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
                   authorityList.add(simpleGrantedAuthority);
               }
           }
    }
           return authorityList;
    }

    //UserDetails返回用户名的方法,我们返回userId,因为后面的很多业务都需要根据userId做操作
    @Override
    public String getUsername(){
        return userId.toString();
    }

    //UserDetails返回密码的方法
    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status == 1;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
    }



}