package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.entily.ProdProp;
import com.pn.entily.ProdPropValue;
import com.pn.service.ProdPropService;
import com.pn.service.ProdPropValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/prod/spec")
@RestController
public class ProdPropController {

    //注入IProdPropService
    @Autowired
    private ProdPropService prodPropService;


    //注入IProdPropValueService
    @Autowired
    private ProdPropValueService prodPropValueService;
///////////==============================================================查询所有商品属性
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public ResponseEntity<List<ProdProp>> loadAllProp() {

        List<ProdProp> prodProps = prodPropService.list();

        return ResponseEntity.ok(prodProps);
    }
    //======================查询所有属性值==========================================
    /*
    url接口/prod/spec/listSpecValue/{id}的请求处理方法:

    同时设置具有prod:spec:page权限的用户可访问该url接口;
   */
    @RequestMapping("/listSpecValue/{id}")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public ResponseEntity<List<ProdPropValue>>
    loadPropValues(@PathVariable("id") Long propId) {
        List<ProdPropValue> prodPropValues =
                prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, propId)
                );

        return ResponseEntity.ok(prodPropValues);
    }
}
