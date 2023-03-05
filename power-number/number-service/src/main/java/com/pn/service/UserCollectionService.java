package com.pn.service;

import com.pn.entily.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
public interface UserCollectionService extends IService<UserCollection>{


    //收藏商品 或取消收藏的方法
    public void addOrCancel(String userId, Long prodId);

}
