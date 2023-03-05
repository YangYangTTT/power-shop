package com.pn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entily.SysLoginUser;


import java.util.List;

public interface SysLoginUserMapper  extends BaseMapper<SysLoginUser> {

    //根据用户id查询用户权限的方法
    public List<String> queryAuthorities(Long userid);
}