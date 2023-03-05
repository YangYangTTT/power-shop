package com.pn.service;

import com.pn.entily.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pn.entily.ProdCommData;

public interface ProdCommService extends IService<ProdComm>{
    //根据商品id查询商品评论数据的业务方法
    public ProdCommData selectProdCommData(Long prodId);

}
