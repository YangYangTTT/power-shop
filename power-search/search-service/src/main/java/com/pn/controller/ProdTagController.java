package com.pn.controller;

import com.pn.entily.ProdTag;
import com.pn.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/prod/prodTag")
@RestController
public class ProdTagController {
    //注入IProdTagService
    @Autowired
    private ProdTagService prodTagService;

    //-------------------------------查询所有商品标签分组的业务方法----------------------------------------------
    @GetMapping("/prodTagList")
    public ResponseEntity<List<ProdTag>> loadFrontTag() {
        //执行业务方法
        List<ProdTag> prodTagList = prodTagService.selectProdTags();

        return ResponseEntity.ok(prodTagList);
    }

}
