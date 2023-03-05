package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.UserAddr;
import com.pn.dao.UserAddrMapper;
import com.pn.service.UserAddrService;

import java.util.Date;
import java.util.List;

@Service
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService{
    //---------------------------------------------根据id查询所有收货地址的方法-----------------------------------
    //注入UserMapper
    @Autowired
    private   UserAddrMapper userAddrMapper;
    @Override
    public List<UserAddr> findUserAddr(String userId) {
  /*
          从user_addr表中,根据user_id列用户id查询status列为1状态正常的,用户的
          所有收货地址,然后按照common_addr列默认地址和create_time列创建时间倒序
          排序;
         */
        List<UserAddr> userAddrs =    userAddrMapper.selectList(new LambdaQueryWrapper<UserAddr>()
                   .eq(UserAddr::getUserId,userId)
                   .eq(UserAddr::getStatus,1)
                   .orderByDesc(UserAddr::getCommonAddr,UserAddr::getCreateTime)
           );
           return  userAddrs;
    }

    ///---------------------------------------------------修改用户默认地址--------------------------------------------------------------------
    @Override
    public void defaultAddr(Long addrId, String userId) {
//逻辑为  1：先根据用户id查询出默认地:2：比较用户默认地址id与传入参数地址id是否相等，相等则判定为误点 直接return
// 不相等则更新当前默认地址common_addr列的值改为0，并根据传来的地址参数 查询出数据  将common_addr列的值改为1并更新

        ///1：先根据用户id查询默认地址
        UserAddr userAddr = userAddrMapper.selectOne(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userId)
                .eq(UserAddr::getCommonAddr, 1)
                .eq(UserAddr::getStatus, 1)
        );
        //2:判断默认地址id与当前参数地址id是否相等   相等return
        if(userAddr.getAddrId().equals(addrId)){
            return;
        }
        //3:不相等  则说明 此地址id为新的默认地址
           //3.1先把通过userid查询的默认地址的common_addr列的值改为0
           userAddr.setCommonAddr(0);
        int i = userAddrMapper.updateById(userAddr);
        //4:判断是否 更新common_addr列的值改为0成功   成功则设置新的默认地址
        if(i>0){
            //先根据传来的地址id参数  查询到信息    修改里面的数据  再更新
            UserAddr userAddr1 = userAddrMapper.selectById(addrId);

            //将其common_addr列的值改为1
            userAddr1.setCommonAddr(1);
            //更新时间改为系统当前时间
            userAddr1.setUpdateTime(new Date());
            //更新默认地址
            userAddrMapper.updateById(userAddr1);

        }

    }
    //==============================新增收货地址=====================================
    //重写save方法


    @Override
    public boolean save(UserAddr userAddr) {
        //设置更新时间为系统当前时间
        userAddr.setUpdateTime(new Date());
        //设置创建时间为系统当前时间
        userAddr.setCreateTime(new Date());
        //设置状态为1
        userAddr.setStatus(1);
        //设置version为0
        userAddr.setVersion(0);
          /*
          从user_addr表中根据user_id列(用户id)查询common_addr列值为1(默认地址)
          的记录的总行数;如果为0,说明用户目前尚未设置默认地址,则将当前新增的地址设
          为默认地址,即common_addr列赋值为1;
         */
        Integer integer = userAddrMapper.selectCount(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userAddr.getUserId())
                .eq(UserAddr::getCommonAddr, 1)
        );
        //如果 integer=0说明没有设置默认地址
        if(integer == 0){
            userAddr.setCommonAddr(1);
        }
        //执行添加
        return userAddrMapper.insert(userAddr) > 0;
    }
}
