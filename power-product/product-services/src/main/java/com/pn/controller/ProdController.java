package com.pn.controller;


import com.pn.entily.ChangeStock;
import com.pn.entily.Prod;
import com.pn.entily.Sku;
import com.pn.service.ProdService;
import com.pn.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/prod/prod")
@RestController
public class ProdController {
    //注入IProdService
    @Autowired
    private ProdService prodService;

    //注入ISkuService
    @Autowired
    private SkuService skuService;
     /*
      post请求方式的url接口/prod/prod的请求处理方法:
      参数@RequestBody Prod prod封装vue发送的添加的商品信息的json数据;

      同时设置具有prod:prod:save权限的用户可访问该url接口;
     */
    @PostMapping
    @PreAuthorize("hasAuthority('prod:prod:save')")
    public ResponseEntity<Void> addProd(@RequestBody Prod prod) {

        prodService.save(prod);

        return ResponseEntity.ok().build();
    }

    //-------------------------------------------远程调用的接口----------------------------------------------------------------
//根据商品id查询商品的url接口/prod/prod/getProdByProdId
    @RequestMapping("/getProdByProdId")
    public Prod getProdByProdId(@RequestParam("proId") Long proId){
        Prod byId = prodService.getById(proId);
        return byId;
    }
//根据商品id查询商品及其所有SKU的方法-----------------------------------------------------
      /*
      url接口/prod/prod/prod/prodInfo的请求处理方法;
      参数Long prodId用户接收请求参数prodId商品id;
     */
@GetMapping("/prod/prodInfo")
public ResponseEntity<Prod> getProdInfo(Long prodId) {

    //执行业务方法
    Prod prod = prodService.selectProdAndSku(prodId);

    return ResponseEntity.ok(prod);
}

//------------------------------------------远程接口调用-------------------------------------------------------------
    //远程调用接口/prod/prod/getSkuByIds,根据skuIds查询所有Sku
    @RequestMapping("/getSkuByIds")
    public List<Sku> getSkuByIds(@RequestBody List<Long> skuIds){
        return skuService.listByIds(skuIds);
    }

    //---------------------------------------远程调用接口（支付业务  减少mysql库存）--------------------------------------------------
  /*
      url接口/prod/prod/updateStock的请求处理方法,用于修改sku表和prod表的库存;
      参数@RequestBody ChangeStock changeStock：用于接收请求参数ChangeStock对象;
     */
    @RequestMapping("/updateStock")
    public void updateStock(@RequestBody ChangeStock changeStock){
//执行业务方法
        prodService.updateStock(changeStock);
    }
}

