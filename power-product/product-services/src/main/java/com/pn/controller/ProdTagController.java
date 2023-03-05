package com.pn.controller;


import com.pn.entily.ProdTag;
import com.pn.service.ProdTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/prod/prodTag")
@RestController
public class ProdTagController {
    //注入IProdTagService
    @Autowired
    private ProdTagService prodTagService;
    /*
       url接口/prod/prodTag/listTagList的请求处理方法:

       同时设置具有prod:prodTag:page权限的用户可访问该url接口;
     */
    @RequestMapping("/listTagList")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    public ResponseEntity<List<ProdTag>> loadAllTag() {

        List<ProdTag> prodTagList = prodTagService.list();

        return ResponseEntity.ok(prodTagList);
    }
}
