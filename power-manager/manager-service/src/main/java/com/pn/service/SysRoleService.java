package com.pn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entily.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysRoleService extends IService<SysRole>{
    ///=================================分页查询所有角色======================
    //Page<susrole>封装的是请求参数页码 和每页行书size
        //   返回值Page<SysRole>封装的是分页的所有信息；
    Page<SysRole> rolesPage(Page<SysRole> page, SysRole role);
    //分页查询角色的业务方法
    public List<SysRole> list() ;

    @Transactional
    Void deleteRoles(List<Long> roleIds);
}
