package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.service.ProdTagService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.ProdTagMapper;
import com.pn.entily.ProdTag;

import java.util.Date;
import java.util.List;

@CacheConfig(cacheNames = "com.cn.service.impi.ProdTagServiceImpl")
@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService {
    //注入ProdTagMapper
    @Autowired
    private ProdTagMapper prodTagMapper;


     /*
      重写list()方法,查询所有状态为1的商品标签分组并按seq列倒序排序,
      并缓存到redis中;
     */
     @Cacheable(key = "'prodTag'")
     @Override
     public List<ProdTag> list() {
         return prodTagMapper.selectList(new LambdaQueryWrapper<ProdTag>()
                 .eq(ProdTag::getStatus, 1)
                 .orderByDesc(ProdTag::getSeq)
         );
     }

    //===================================新增商品标签的方法===============================
    @CacheEvict(key = "'prodTag'")//新增商品标签的方法在新增之前先清除缓存
    //重写save()方法
    @Override
    public boolean save(ProdTag prodTag) {

        //设置商品标签分组的创建时间
        prodTag.setCreateTime(new Date());
        //设置商品标签分组的更新时间
        prodTag.setUpdateTime(new Date());
        //设置shop_id列的值为1
        prodTag.setShopId(1L);

        //添加商品标签分组
        return prodTagMapper.insert(prodTag) > 0;
    }
}
