package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.ProdTagMapper;
import com.pn.entily.ProdTag;
import com.pn.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService{

    //注入ProdTagMapper
    @Autowired
    private ProdTagMapper prodTagMapper;

    /////---------------------------------------查询所有商品标签分组的方法---------------------------------------------------
    @Override
    public List<ProdTag> selectProdTags() {
 /*
          从prod_tag表中查询status列值为1(状态正常)的所有id列(商品标签分组id)、
          title列(商品标签分组名称)、style列(商品标签分组的风格[一行几列])的值,
          然后将查询结果按照seq列倒序排序;
         */
        List<ProdTag> prodTags = prodTagMapper.selectList(new LambdaQueryWrapper<ProdTag>()
                .select(ProdTag::getId,ProdTag::getTitle ,ProdTag::getStyle)
                .eq(ProdTag::getStatus, 1)
                .orderByDesc(ProdTag::getSeq)
        );
        return prodTags;
    }
}
