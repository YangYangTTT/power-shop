package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.StoreProductFeign;
import com.pn.entily.Prod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.IndexImgMapper;
import com.pn.entily.IndexImg;
import com.pn.service.IndexImgService;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService{

    //注入StoreProudctFegin
    @Autowired
    private StoreProductFeign storeProductFeign;

    @Autowired
    private  IndexImgMapper indexImgMapper;
    //-------------------------------------分页查询轮播图---------------------------------------------------------------------------------
    @Override
    public Page<IndexImg> findIndexImgPage(Page<IndexImg> page, IndexImg indexImg) {
        page  = indexImgMapper.selectPage(page, new LambdaQueryWrapper<IndexImg>()
                .eq(!ObjectUtils.isEmpty(indexImg.getStatus()), IndexImg::getStatus, indexImg.getStatus())
                .orderByDesc(IndexImg::getSeq)
        );
        return page;
    }
    //------------------------------------------保存轮播图------------------------------------------------------------
    //重写save()方法
    @Override
    public boolean save(IndexImg indexImg) {
        //设置shop_id列值为1
        indexImg.setShopId(1L);
        //如果状态status正常为true,则设置上传时间uploadTime为系统当前时间
        if (indexImg.getStatus()) {
            indexImg.setUploadTime(new Date());
        }
        //完成添加
        return indexImgMapper.insert(indexImg) > 0;
    }
////-------------------------------------------------------------远程调用-----------------------------
    //重写getbyid方法
@Override
public IndexImg getById(Serializable imgId) {
    //根据轮播图id查询轮播图信息
    IndexImg indexImg = indexImgMapper.selectById(imgId);
    if(!ObjectUtils.isEmpty(indexImg)){
        //拿到轮播图类型所属的商品id
        Long prodId = indexImg.getRelation();
        if (!ObjectUtils.isEmpty(prodId)) {
            //远程调用product-service服务的url接口/prod/prod/getProdByProdId的方法
            //                  getProdByProdId(),根据商品id查询商品
            Prod prod = storeProductFeign.getProdByProdId(prodId);
            if (!ObjectUtils.isEmpty(prod)) {
                //将查询到的商品名称和图片url地址存放到indexImg对象中
                String prodName = prod.getProdName();
                String pic = prod.getPic();
                indexImg.setProdName(prodName);
                indexImg.setPic(pic);
            }
        }
        }
    return indexImg;
}
////============================轮播图展示===================================
    //重写list()方法

    @Override
    public List<IndexImg> list() {
   /*
          小程序的性能其实很差,所以不要给其响应多余的数据,即其需要哪些数据就给其响应
          哪些数据;

          对于查询的轮播图,小程序只需要其url地址和所属商品的id;所以下面的sql,查询的
          是status列值为1(状态正常)的所有轮播图的img_url列(url地址)和relation列(
          所属商品id)的值,且将查询结果按照seq列倒序排序;
         */
        List<IndexImg> indexImgs = indexImgMapper.selectList(
                new LambdaQueryWrapper<IndexImg>()
                        .select(IndexImg::getImgUrl, IndexImg::getRelation)
                        .eq(IndexImg::getStatus, 1)
                        .orderByDesc(IndexImg::getSeq)
        );

        return indexImgs;
    }
}

