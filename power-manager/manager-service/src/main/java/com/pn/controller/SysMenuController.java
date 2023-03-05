package com.pn.controller;


import com.pn.service.SysMenuService;
import entily.SysMenu;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.MenuVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequestMapping("/sys/menu")
@RestController
public class SysMenuController {
//注入service
    @Autowired
    private SysMenuService service;

    @RequestMapping("/nav")
    public ResponseEntity<MenuVo> loadMenuTree(){
      /////1拿用户菜单树

        //拿到当前登陆用户id
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<SysMenu> menusTreeList = service.getMenusTree(Long.parseLong(userId));

        ////2拿用户权限
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                     List<String> list=new ArrayList<>();
        for(GrantedAuthority grantedAuthority:authorities){
            list.add(grantedAuthority.getAuthority());
        }
           //组装数据
        MenuVo menuVo = new MenuVo(menusTreeList, list);

        ///响应
       return   ResponseEntity.ok(menuVo);

    }

}
