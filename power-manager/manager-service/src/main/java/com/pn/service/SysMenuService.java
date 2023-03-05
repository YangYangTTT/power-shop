package com.pn.service;

import entily.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu>{
//////根据用户id查询用户的权限菜单树    ----在业务层中查询到用户的所有的权限菜单生成一个菜单树list
    public List<SysMenu> getMenusTree(Long userId);

}
