package com.pn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.service.SysMenuService;

import com.pn.service.SysUserService;
import entily.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sys/user")
@RestController
public class SysUserController {


    ///注入service
    @Autowired
    private SysUserService service;


    //url接口/sys/user/info
    @RequestMapping("/info")
    public ResponseEntity<SysUser> queryLoginUser(){
        //拿到当前登录的用户id
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //根据用户id查询用户

        SysUser user = service.getById(Long.parseLong(userId));
     //响应
        return ResponseEntity.ok(user);
    }
    ////===================================分页请求===============================================
    @PreAuthorize("hasAuthority('sys:user:page')")
    @RequestMapping("/page")
    public ResponseEntity<Page<SysUser>>  loadUserPage(Page<SysUser> page, SysUser sysUser){
                 Page<SysUser> userPage=service.loadUserPage(page,sysUser);
            return ResponseEntity.ok(userPage);
    }
/////===================================添加管理员用户  在SysRoleController先查询角色==================
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    @PostMapping
    public  ResponseEntity<Void>saveUser(@RequestBody  SysUser sysUser){
            //执行业务
        service.save(sysUser);
        //响应
        return  ResponseEntity.ok().build();
    }
    //=====================================批量删除用户=================================
 ///将所有ids保存到List<Logn>中
@PreAuthorize("hasAnyAuthority('sys:user:delete')")
@DeleteMapping("/{ids}")//将占位符存到ids中
    public ResponseEntity<Void>deleteUser( List<Long> ids){
    ////执行业务
    service.deleteUser(ids);
    //响应
    return  ResponseEntity.ok().build();
}
//=================================更新用户============================
///先查询此用户的信息通过userid
    @PreAuthorize("hasAnyAuthority('sys:user:info')")
    @RequestMapping("/info/{id}")
   public   ResponseEntity<SysUser>queryUser(@PathVariable Long id){
        SysUser user = service.getById(id);
        //做出响应
        return ResponseEntity.ok(user);
    }
  /////再修改用户
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody SysUser user){
service.updateById(user);
        //做响应
        return ResponseEntity.ok().build();
    }

}
