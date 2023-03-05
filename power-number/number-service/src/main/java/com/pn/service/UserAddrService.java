package com.pn.service;

import com.pn.entily.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAddrService extends IService<UserAddr>{

//根据用户id查询所有收货地址
    public List<UserAddr> findUserAddr(String userId);

    ////修改用户默认地址(参数为)
    public  void  defaultAddr(Long addrId,String userId);
}
