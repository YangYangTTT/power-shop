package com.pn.controller;


import com.pn.entily.ProdCommData;
import com.pn.service.ProdCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/prod/prodComm")
@RestController
public class ProdCommController {
    //注入IProdCommService
    @Autowired
    private ProdCommService prodCommService;

    /*
    url接口/prod/prodComm/prodComm/prodCommData的请求处理方法;
    参数Long prodId用于接收请求参数prodId商品id;
   */
    @GetMapping("/prodComm/prodCommData")
    public ResponseEntity<ProdCommData> getProdCommData(Long prodId) {

        //执行业务方法
        ProdCommData prodCommData = prodCommService.selectProdCommData(prodId);

        return ResponseEntity.ok(prodCommData);
    }
}
