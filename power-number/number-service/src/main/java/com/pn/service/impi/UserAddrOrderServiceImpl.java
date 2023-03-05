package com.pn.service.impi;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.UserAddrOrderMapper;
import com.pn.entily.UserAddrOrder;
import com.pn.service.UserAddrOrderService;
@Service
public class UserAddrOrderServiceImpl extends ServiceImpl<UserAddrOrderMapper, UserAddrOrder> implements UserAddrOrderService{

}
