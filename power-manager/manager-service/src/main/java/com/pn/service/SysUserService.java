package com.pn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entily.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysUserService extends IService<SysUser>{
    /////查询user对象  并分页
    public Page<SysUser> loadUserPage(Page<SysUser> page, SysUser sysUser);

    //-=============================删除用户   同时删除用户角色关系===========================
    @Transactional
    void deleteUser(List<Long> userIds);
}


