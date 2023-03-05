package com.pn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.ChangeStock;
import com.pn.entily.Prod;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProdService extends IService<Prod>{


    List<Prod> selectProdPageForEs(Page<Prod> page);

    //根据商品id查询商品及其所有SKU的方法
    public Prod selectProdAndSku(Long prodId);

    //减mysql库存的方法
    public  void updateStock(ChangeStock changeStock);
}
