package com.pn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entily.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    //根据用户id查询其所有权限菜单的方法
    public List<SysMenu> allMenusByUid(Long userId);


}