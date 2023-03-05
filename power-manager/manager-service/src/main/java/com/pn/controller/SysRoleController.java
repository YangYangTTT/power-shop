package com.pn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.dao.SysRoleMapper;
import com.pn.service.SysRoleService;
import entily.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sys/role")
@RestController
public class SysRoleController {

    //注入sysrolemapper
    @Autowired
    private SysRoleService sysRoleService;



    //-----(新增管理员用户)   首先需要查询出所有的角色   因为添加用户时要选   然后在SysUserController添加管理员用户
    @PreAuthorize("hasAnyAuthprity('sys:role:page')")
@RequestMapping("/list")///查询所有角色（增加用户和修改用户都会用到）
    public ResponseEntity<List<SysRole>> roleList(){
     ///执行查询
    List<SysRole> roleList=sysRoleService.list();
   ///响应
    return ResponseEntity.ok(roleList);
    }
    //====================================分页查询所有角色======================
    @PreAuthorize("hasAnyAuthprity('sys:rple:page')")
@RequestMapping("/page")
    public ResponseEntity<Page<SysRole>> rolesPage(Page<SysRole> page, SysRole role){
        //执行业务
        Page<SysRole> rolePage = sysRoleService.rolesPage(page, role);
        //做响应
        return ResponseEntity.ok(rolePage);
    }
/////=========================添加角色（先查询所有菜单 并缓存与redis中）====================================
//url接口sys/role ---- 添加角色
@PreAuthorize("hasAnyAuthority('sys:role:save')")
@PostMapping
public ResponseEntity<Void> saveRole(@RequestBody SysRole role){
    //执行业务
    sysRoleService.save(role);
    //做响应
    return ResponseEntity.ok().build();
}
//////=================================删除角色 （需要先查询所有meanulist）=================
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    @DeleteMapping
    public ResponseEntity<Void>deleteRole(@RequestBody List<Long> roleIds){
 sysRoleService.deleteRoles(roleIds);
 return ResponseEntity.ok().build();
    }
}
