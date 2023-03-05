package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.dao.SysUserRoleMapper;
import com.pn.service.SysUserRoleService;
import entily.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.SysUserMapper;
import entily.SysUser;
import com.pn.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends  ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    ///注入sysusermapper
    @Autowired
    private SysUserMapper sysUserMapper;
    ////查询所有user并分页

    //注入SysUserRoleService --- 批量添加方法只有service有
    @Autowired
    private SysUserRoleService userRoleService;

    //注入SysUserRoleMapper
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public Page<SysUser> loadUserPage(Page<SysUser> page, SysUser sysUser) {
        /*
         * 如果  参数sysuser对象的username属性名不为空  则是按照用户名查询，则按照username模糊查询并分页
         * 出管理员用户，然后按用户创建时间降序
         * 如果sysuser对象的username属性名为空  则是菜单管理员列表查询用户   直接分页查询出管理员用户
         * */
        page = sysUserMapper.selectPage(page, new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(sysUser.getUsername()), SysUser::getUsername,
                        sysUser.getUsername())
                .orderByDesc(SysUser::getCreateTime)
        );
        return page;

    }

    //------------------------------------------------添加管理员用户---------------------------------------------------------------------------------------------------------
    @Transactional
    @Override
    public boolean save(SysUser user) {
        ////对密码作加密请求
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        ///给定create_user_id
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        user.setCreateUserId(Long.parseLong(userId));
        //给定create_time
        user.setCreateTime(new Date());
        //给定shop_id --- 我们的商城项目中店铺只有一个,所以shop_id只有1
        user.setShopId(1L);
        ///添加用户
        int i = sysUserMapper.insert(user);
        ///判断是否添加成功    成功的话 将用户选择的角色用户关系存入sys_user_role表
        if (i > 0) {
            List<SysUserRole> userRoleList = new ArrayList<>();///存放user 与role关系的list
            List<Long> list = user.getRoleIdList();  ///查询到所有角色id的list
            for (Long roleId : list) {
                SysUserRole sysUserRole = new SysUserRole();//////一个SysUserRole对象就是一个用户关系
                sysUserRole.setUserId(user.getUserId());
                sysUserRole.setRoleId(roleId);
            }
            ///执行方法存储
            userRoleService.saveBatch(userRoleList);
        }
        return i > 0;

    }

    //-=============================删除用户   同时删除用户角色关系===========================
    @Transactional
    @Override
    public void deleteUser(List<Long> userIds) {
        ///执行方法   根据用户id批量删除用户
        int i = sysUserMapper.deleteBatchIds(userIds);
        /////判断是否删除成功   成功的话删除  用户角色表
        if (i > 0) {
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .in(SysUserRole::getUserId, userIds)
            );
        }

    }

    ///===================================更新用户=========================================
    //需要先查询所有角色  一共多选框选择（在增加管理员时此方法已经写过） 然后根据id更新信息   最后修改user-role对应表
    ///思路为先删除对应信息   然后 再添加
    @Transactional
    @Override
    public boolean updateById(SysUser user) {
        //先对密码作加密处理    然后  修改
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        int i = sysUserMapper.updateById(user);
        //判断是否更新成功   成功 则操作user-role表
        if (i > 0) {
            ////修改用户角色关系
            int delete = userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getUserId())
            );
////判断  是否修改成功
            if (delete > 0) {
                List<SysUserRole> list = new ArrayList<>();
                List<Long> roleids = new ArrayList<>();
                for (Long l : roleids) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setRoleId(l);
                    sysUserRole.setUserId(user.getUserId());
                    list.add(sysUserRole);
                }
                userRoleService.saveBatch(list);
            }

        }
        return i > 0;
    }
}
