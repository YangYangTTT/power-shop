package com.pn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entily.Sku;
import feign.Param;

public interface SkuMapper extends BaseMapper<Sku> {
    //减mysql库存的方法---->修改sku表库存的方法      修改prod表在prodMapper
      public int updateStock(@Param("skuId")Long skuId,@Param("count")Integer count);

}