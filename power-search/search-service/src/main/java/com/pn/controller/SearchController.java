package com.pn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.EsProd;
import com.pn.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    //注入SearchService
    @Autowired
    private SearchService searchService;
     /*
      url接口/prod/prodListByTagId的请求处理方法;
      参数Long tagId：接收请求参数tagId商品标签分组id;
      参数Integer size：接收请求参数每页行数;
      参数@RequestParam(required = false, defaultValue = "1") Integer current：
      接口请求参数current页码,如果没有传递请求参数current则默认值给1;
     */
     //-----------------------------------------  根据商品标签分组id从ES中分页查询该分组下的商品的业务方法;----------------------------------------
    @GetMapping("/prod/prodListByTagId")
public ResponseEntity<Page<EsProd>>prodListByTagId(  Long tagId, Integer size,@RequestParam(required = false,defaultValue = "1") Integer current){
        //执行业务方法
        Page<EsProd> prodEsPage =searchService.searchProdByTagId(tagId, current, size);
        return ResponseEntity.ok(prodEsPage);
    }
    //------------------------------------------------------------ 根据商品名称从ES中分页查询商品的业务方法------------------------------------------
    @GetMapping("/search/searchProdPage")
    public ResponseEntity<Page<EsProd>> searchProdPage(String prodName, Integer current,
                                                       Integer size,Integer sort){
        //执行业务方法
        Page<EsProd> page = searchService.searchProdByProdName(prodName, current, size, sort);
        return ResponseEntity.ok(page);
    }
}
