package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.dao.EsProdDao;
import com.pn.entily.EsProd;
import com.pn.entily.ProdCommData;
import com.pn.service.ProdCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.ProdCommMapper;
import com.pn.entily.ProdComm;

import java.math.BigDecimal;

@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService {
///查询商品各评论数  以及好评率的方法-------------------------------------------------------------------------------------
//注入ProdCommMapper
@Autowired
private ProdCommMapper prodCommMapper;

    //注入EsProdDao
    @Autowired
    private EsProdDao esProdDao;
    @Override
    public ProdCommData selectProdCommData(Long prodId) {
        //从ES中根据商品id查询到商品,然后再拿到商品的好评数和好评率
        EsProd esProd = esProdDao.findById(prodId).get();
        Long praiseNumber = esProd.getPraiseNumber();//好评数
        BigDecimal positiveRating = esProd.getPositiveRating();//好评率
         /*
          查询商品的评论总数：
          从prod_comm表中根据prod_id列(商品id)查询status列值为1(状态正常)的记录
          行数,即商品的评论总数;
         */
        Integer allCount = prodCommMapper.selectCount(
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
        );
          /*
          查询商品的中评数：
          从prod_comm表中根据prod_id列(商品id)查询status列值为1(状态正常)
          evaluate列值为1(中评)的记录行数,即商品的中评数;
         */
        Integer secondCount = prodCommMapper.selectCount(
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
                        .eq(ProdComm::getEvaluate, 1)
        );
          /*
          查询商品的差评数：
          从prod_comm表中根据prod_id列(商品id)查询status列值为1(状态正常)
          evaluate列值为2(差评)的记录行数,即商品的差评数;
         */
        Integer badCount = prodCommMapper.selectCount(
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
                        .eq(ProdComm::getEvaluate, 2)
        );
        /*
          查询商品的带图评论数：
          从prod_comm表中根据prod_id列(商品id)查询status列值为1(状态正常)
          pics列不为空(说明带图)的记录行数,即商品的带图评论数;
         */
        Integer picCount = prodCommMapper.selectCount(
                new LambdaQueryWrapper<ProdComm>()
                        .eq(ProdComm::getProdId, prodId)
                        .eq(ProdComm::getStatus, 1)
                        .isNotNull(ProdComm::getPics)
        );
        //将评论数据组装到ProdCommData对象中并返回
        ProdCommData prodCommData = new ProdCommData();
        prodCommData.setPraiseNumber(praiseNumber);
        prodCommData.setPositiveRating(positiveRating);
        prodCommData.setNumber(allCount);
        prodCommData.setSecondaryNumber(secondCount);
        prodCommData.setNegativeNumber(badCount);
        prodCommData.setPicNumber(picCount);

        return prodCommData;
    }
}
