package com.pn.service.impi;

import com.pn.dao.ProdPropValueMapper;
import com.pn.entily.ProdPropValue;
import com.pn.service.ProdPropService;
import com.pn.service.ProdPropValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.ProdProp;
import com.pn.dao.ProdPropMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impi.ProdPropServiceImpl")
@Service
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService {
////点击新增按钮  查询所有商品属性并缓存到redis，查询所有商品标签分组并缓存到redis。
//注入ProdPropMapper
@Autowired
private ProdPropMapper prodPropMapper;

    //注入ProdPropValueMapper
    @Autowired
    private ProdPropValueMapper prodPropValueMapper;

    //注入IProdPropValueService
    @Autowired
    private ProdPropValueService prodPropValueService;


    //重写list()方法,查询所有商品属性,并缓存到redis中;
    @Override
    @Cacheable(key = "'prodProp'")
    public List<ProdProp> list() {
        return prodPropMapper.selectList(null);
    }
//=============================新增属性的方法===========================
    @CacheEvict(key = "'prodProp'")/////新增属性的方法在新增之前就得先清除缓存
    @Override
    @Transactional
    public boolean save(ProdProp prodProp) {
        //shop_id列直接设置为1
        prodProp.setShopId(1L);
        //rule列也直接设置为1
        prodProp.setRule((byte) 1);
        //添加属性
        int i = prodPropMapper.insert(prodProp);
        ////添加属性成功后添加属性值
        if(i>0){
            //拿到属性对象ProdProp中保存的所有属性值的List<ProdPropValue>
            List<ProdPropValue> prodPropValues = prodProp.getProdPropValues();
            //给每个属性值对象ProdPropValue设置其所属属性的id
            for(ProdPropValue p:prodPropValues){
                  p.setPropId(prodProp.getPropId());
            }
            //批量添加属性值
            prodPropValueService.saveBatch(prodPropValues);
        }
        return i > 0;
    }
}
