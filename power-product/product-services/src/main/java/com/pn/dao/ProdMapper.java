package com.pn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pn.entily.Prod;
import feign.Param;

public interface ProdMapper extends BaseMapper<Prod> {
    ////减mysql库存的方法---->修改prod表库存的方法
    public int  updateStock(@Param("prodId") Long prodId,@Param("count") Integer count);
}