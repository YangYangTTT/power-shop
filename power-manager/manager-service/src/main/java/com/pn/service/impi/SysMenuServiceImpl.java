package com.pn.service.impi;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import entily.SysMenu;
import com.pn.dao.SysMenuMapper;
import com.pn.service.SysMenuService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{


    //注入mapper
    @Autowired
    private SysMenuMapper sysMenuMapper;
    ////注入redis
    @Autowired
    private StringRedisTemplate redisTemplate;
    ////将查询寻到的菜单树list存入redis中    登录完成后 从redis中查询有无菜单树 有则返回  无则查询 生成菜单树
    ////================================新增角色第一步（查询所有菜单并缓存）===========================
    @Override
    public List<SysMenu> getMenusTree(Long userId) {
      //存储到redis中的菜单树  键为meun:用户id       要用id区分用户     值为菜单树list转换的json字符串
        String jsonMenuTree = redisTemplate.opsForValue().get("menu" + userId);
        List<SysMenu> menuTreeList=null;
        if(StringUtils.hasText(jsonMenuTree)){ //redis中有菜单list    则转换为json格式
           menuTreeList = JSON.parseArray(jsonMenuTree, SysMenu.class);
        }else{//redis中没有菜单树
            List<SysMenu> sysMenus = sysMenuMapper.allMenusByUid(userId);///查询
       menuTreeList= allMenuToMenuTree(sysMenus,0L);//将所有meanu转换为菜单树list(自定义递归方法转换)
         redisTemplate.opsForValue().set("mnu"+userId,JSON.toJSONString(menuTreeList));
        }
return    menuTreeList;
    }

    private List<SysMenu> allMenuToMenuTree(List<SysMenu> sysMenus, long pid) {
              ////从所有菜单中拿到一级菜单
        List<SysMenu> firstLevelMenus = new ArrayList<>();
        for(SysMenu sysMenu:sysMenus){
            if(sysMenu.getParentId().equals(pid)){
                firstLevelMenus.add(sysMenu);
            }

        }
           for(SysMenu sysMenuss:firstLevelMenus){
               List<SysMenu> sysMenus1 = allMenuToMenuTree(sysMenus, sysMenuss.getMenuId());
          sysMenuss.setList(sysMenus1);
           }
           return firstLevelMenus;
    }
}
