package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.UserCollectionMapper;
import com.pn.entily.UserCollection;
import com.pn.service.UserCollectionService;

import java.util.Date;

@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService{
    //注入UserCollectionMapper
    @Autowired
    private UserCollectionMapper userCollectionMapper;

    /////------------------------------收藏商品 或取消收藏的方法-------------------------------------------------------
    @Override
    public void addOrCancel(String userId, Long prodId) {


        ///先查询用户是否收藏该商品
        UserCollection userCollection = userCollectionMapper.selectOne(
                new LambdaQueryWrapper<UserCollection>()
                        .eq(UserCollection::getUserId, userId)
                        .eq(UserCollection::getProdId, prodId)
        );
        ////如果查询到的记录不为空  则说明用户已经收藏该商品     然后进行取消收藏操作
        if (!ObjectUtils.isEmpty(userCollection)) {
            userCollectionMapper.deleteById(userCollection.getId());
        }
        //为空则说明此商品未收藏   然后将商品收藏
        UserCollection newUserCollection = new UserCollection();
        newUserCollection.setUserId(userId);
        newUserCollection.setProdId(prodId);
        newUserCollection.setCreateTime(new Date());
        userCollectionMapper.insert(newUserCollection);
    }
    }

