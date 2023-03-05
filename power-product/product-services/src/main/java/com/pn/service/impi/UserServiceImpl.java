package com.pn.service.impi;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.WxUser;
import com.pn.dao.WxUserMapper;
import com.pn.service.UserService;

import java.util.Collection;

@Service
public class UserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements UserService{

    @Override
    public boolean save(WxUser entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<WxUser> entityList) {
        return super.saveBatch(entityList);
    }
}
