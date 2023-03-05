package com.pn.service.impi;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.service.SysRoleMenuService;
import entily.SysRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.SysRoleMapper;
import entily.SysRole;
import com.pn.service.SysRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{
    /////////查询所有角色的方法  （添加管理员用户时使用）
 //   注入SysRoleMapper
    @Autowired
    private SysRoleMapper sysRoleMapper;
            //注入redis   先从redis中查询  没有了  再到数据库查询   人后存进redis
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private  SysRoleMenuServiceImpl service;


    //注入SysRoleMenuService
    //=================================查询所有角色=====================================================
@Autowired
private SysRoleMenuService roleMenuService;
    @Override
    public List<SysRole> list() {
            //先判断redis中有没有
        String jsonRoleList=   redisTemplate.opsForValue().get("role:all");
        List<SysRole> roleList = null;
         if(StringUtils.hasText(jsonRoleList)){
             ///将json串转回List<SysRole>
            roleList = JSON.parseArray(jsonRoleList, SysRole.class);
         }else{ //redis中没有
           //从数据库查询
             roleList=  sysRoleMapper.selectList(null);
             //存入redis中
             redisTemplate.opsForValue().set("role:all",JSON.toJSONString(roleList));
         }
        return roleList;
    }

///=================================分页查询所有角色======================
//Page<susrole>封装的是请求参数页码 和每页行书size
    //   返回值Page<SysRole>封装的是分页的所有信息；
@Override
public Page<SysRole> rolesPage(Page<SysRole> page, SysRole role){
    return sysRoleMapper.selectPage(page,
            new LambdaQueryWrapper<SysRole>()
                    .like(StringUtils.hasText(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
                    .orderByDesc(SysRole::getCreateTime)
           );
}
//========================================添加角色的方法（需先查到所有menulist）============================
    @Transactional
    @Override
    public boolean save(SysRole role){
            //补充角色信息
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                    //角色将id设置进role
            role.setCreateUserId(Long.parseLong(userId));
        role.setCreateTime(new Date());
        //添加角色
        int insert = sysRoleMapper.insert(role);
        //判断 如果添加成功  则清除redis中的角色信息   并修改角色菜单表
        if(insert>0){
             redisTemplate.delete("role:all");
                //添加角色菜单关系
     List<SysRoleMenu> list=new ArrayList<>();
     List<Long> menuIdList=new ArrayList<>();
      for(Long l:menuIdList){
          SysRoleMenu sysRoleMenu=new SysRoleMenu();
          sysRoleMenu.setRoleId(role.getRoleId());
          sysRoleMenu.setMenuId(l);
          list.add(sysRoleMenu);
      }
          //执行方法   添加角色
            boolean b = service.saveBatch(list);
        }
        return insert>0;
    }
    /////====================================删除角色（根据roleids删除角色）然后再修改角色和meanu关联表=====================

    @Override
    @Transactional
    public Void deleteRoles(List<Long> roleIds) {
      ///删除角色
        int i = sysRoleMapper.deleteBatchIds(roleIds);
        ///判断是否删除成功   成功则删除角色meanu关联表
        if(i>0){
        ///清除redis中的角色缓存
            redisTemplate.delete("role:all");
         ///删除菜单和role关联表
            service.remove(new LambdaQueryWrapper<SysRoleMenu>()
                    .in(SysRoleMenu::getRoleId,roleIds)
            );
        }
        return null;
    }
}
