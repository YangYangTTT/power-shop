package com.pn.service.impi;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.dao.ProdCommMapper;
import com.pn.dao.SkuMapper;
import com.pn.entily.*;
import com.pn.service.ProdService;
import com.pn.service.ProdTagReferenceService;
import com.pn.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.ProdMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {


    //注入ProdMapper
    @Autowired
    private ProdMapper prodMapper;

    //注入SkuService
    @Autowired
    private SkuService skuService;

    //注入IProdTagReferenceService
    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    //注入ProdCommMapper
    @Autowired
    private ProdCommMapper prodCommMapper;
    //注入SkuMapper
    @Autowired
    private SkuMapper skuMapper;


    //==============================================新增产品============================================
    //重写save()方法,并进行事务处理
    @Override
    @Transactional
    public boolean save(Prod prod) {
        //设置sold_num列值为0
        prod.setSoldNum(0);
        //设置shop_id列值为1
        prod.setShopId(1L);
        //设置创建时间和更新时间为系统当前时间
        prod.setCreateTime(new Date());
        prod.setUpdateTime(new Date());
        //如果商品状态值为1,则设置上架时间为系统当前时间
        if(prod.getStatus().equals(1)){
            prod.setPutawayTime(new Date());
        }
        //设置version列值为0
        prod.setVersion(0);

        //  将接收了配送方式的Prod.DeliveryModeVo对象转成json串添加到delivery_mode列
        Prod.DeliveryModeVo deliveryModeVo=prod.getDeliveryModeVo();
        String delivery  = JSON.toJSONString(deliveryModeVo);
        prod.setDeliveryMode(delivery);
        //添加商品
        int i = prodMapper.insert(prod);
        if(i>0){
            //处理产品分组即商  品标签信息,添加商品标签关系
            handlerTag(prod.getProdId(),prod.getTagList());
            //处理SKU信息,添加SKU信息
            handlerSku(prod.getProdId(), prod.getSkuList());
        }
        return i > 0;
    }



    public   void  handlerTag(Long prodId, List<Long> tagList){
             if(!CollectionUtils.isEmpty(tagList)){
                   List<ProdTagReference> prodTagReferences=new ArrayList<>();
                   for(Long tagid :tagList){
                       ProdTagReference prodTagReference=new ProdTagReference();
                       //设置shop_id列值为1
                       prodTagReference.setShopId(1L);
                       //设置tag_id
                       prodTagReference.setTagId(tagid);
                       //设置prod_id
                       prodTagReference.setProdId(prodId);
                       //设置创建时间为系统当前时间
                       prodTagReference.setCreateTime(new Date());
                       //设置status列值为true
                       prodTagReference.setStatus(true);
                                prodTagReferences.add(prodTagReference);
                   }
                 //批量添加
                 prodTagReferenceService.saveBatch(prodTagReferences);
             }

            }


    private void handlerSku(Long prodId, List<Sku> skuList) {
        if(!CollectionUtils.isEmpty(skuList)){
            for (Sku sku : skuList) {
                //设置prod_id
                sku.setProdId(prodId);
                //设置创建时间为系统当前时间
                sku.setRecTime(new Date());
                //设置更新时间为系统当前时间
                sku.setUpdateTime(new Date());
                //设置version列值为0
                sku.setVersion(0);
                //设置actual_stocks列值为stocks列值
                sku.setActualStocks(sku.getStocks());
                //设置is_delete列值为0
                sku.setIsDelete((byte) 0);
                //设置status列值为1
                sku.setStatus((byte) 1);
            }
            //批量添加
            skuService.saveBatch(skuList);
        }
    }
/////==============================分页查询数据导入es===========================================
    @Override
    public List<Prod> selectProdPageForEs(Page<Prod> page) {
        //完成分页查询
        Page<Prod> prodPage = prodMapper.selectPage(page, null);
        //拿到查询到的商品集合
        List<Prod> prodList = prodPage.getRecords();

        if (CollectionUtils.isEmpty(prodList)) {
            return Collections.emptyList();
        }
        /*处理标签*/
        ////拿到所有商品id
        List<Long> prodIds = new ArrayList<>();
        for (Prod prod : prodList) {
            prodIds.add(prod.getProdId());
        }
        //从商品标签中间表中查询出所有商品标签关系
        List<ProdTagReference> prodTagReferences = prodTagReferenceService.list(new LambdaQueryWrapper<ProdTagReference>()
                .in(ProdTagReference::getProdId, prodIds)
        );
        //将每个商品的所有标签存入List<Long>中,然后赋值给每个商品对象的List<Long>属性tagList
        for (Prod prod : prodList) {
            List<Long> tagIds = new ArrayList<>();
            for (ProdTagReference prodTagReference : prodTagReferences) {
                if (prod.getProdId().equals(prodTagReference.getProdId())) {
                    tagIds.add(prodTagReference.getTagId());
                }
            }
            prod.setTagList(tagIds);
        }
         /*
        处理好评数和好评率
                */
        //查询出所有商品的评论
        List<ProdComm> prodComms = prodCommMapper.selectList(
                new LambdaQueryWrapper<ProdComm>()
                        .in(ProdComm::getProdId, prodIds)
        );
        //对每个商品的评论进行处理
        for (Prod prod : prodList) {
            ////拿到每个商品的所有评论
            List<ProdComm> prodCommList = new ArrayList<>();
            for (ProdComm prodComm : prodComms) {
                if (prod.getProdId().equals(prodComm.getProdId())) {
                    prodCommList.add(prodComm);
                }
            }
            //计算好评数和好评列率
            if (!CollectionUtils.isEmpty(prodCommList)) {
                //统计好评数
                long goodCount = 0;
                for (ProdComm prodComm : prodCommList) {
                    if (prodComm.getEvaluate().equals(0)) {
                        goodCount++;
                    }
                }
                //统计好评率
                //评论总数
                int commSize = prodCommList.size();
                //好评数/评论总数,四舍五入保留两位小数,再乘以100
                BigDecimal goodLv = new BigDecimal(goodCount)
                        .divide(new BigDecimal(commSize), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100));

                //将好评数和好评率注入给Prod对象
                prod.setPraiseNumber(goodCount);
                prod.setPositiveRating(goodLv);
            }
        }
        return  prodList;
    }
//-----------------------------------------------------//根据商品id查询商品及其所有SKU的方法-------------------------------
    @Override
    public Prod selectProdAndSku(Long prodId) {
        //通过商品id查询出商品
        Prod prod = prodMapper.selectById(prodId);
        //从sku表中根据prod_id列(商品id)查询出status列值为1(状态正常)的商品的所有sku
        List<Sku> skus =  skuService.list(new LambdaQueryWrapper<Sku>()
                .eq(Sku::getProdId, prodId)
                .eq(Sku::getStatus, 1)
        );
        //将查询到的所有sku保存到商品
        prod.setSkuList(skus);
        return prod;
    }

    //--------------------------------------------------减mysql库存的方法-----------------------------------------------------------
    //该方法涉及到多条更新语句  需要加事务
    @Transactional
    @Override
    public void updateStock(ChangeStock changeStock) {
         //从参数中拿到skuChange集合
        List<SkuChange> skuChanges = changeStock.getSkuChanges();
        //遍历集合
        for(SkuChange skuChange:skuChanges){
            //调用skuMapper方法执行更新业务
            int i = skuMapper.updateStock(skuChange.getSkuId(), skuChange.getCount());
            if(i<=0){
                throw  new RuntimeException("库存不足");
            }
        }
        //从参数中拿到getProdChanges
        List<ProdChange> prodChanges = changeStock.getProdChanges();
        for(ProdChange prodChange:prodChanges){
            int i = prodMapper.updateStock(prodChange.getProdId(), prodChange.getCount());
            if(i<=0){
                throw  new RuntimeException("库存不足");
            }
        }
    }
}
