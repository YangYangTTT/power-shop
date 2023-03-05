package com.pn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pn.entily.ProdTag;

import java.util.List;

public interface ProdTagService extends IService<ProdTag>{
//查询所有商品标签分组的业务方法
    public List<ProdTag> selectProdTags();

}
