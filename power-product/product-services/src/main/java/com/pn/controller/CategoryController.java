package com.pn.controller;


import com.pn.entily.Category;
import com.pn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/prod/category")
@RestController
public class CategoryController {

    ////注入CategoryService
    @Autowired
    private CategoryService categoryService;
    ////查询所有商品分类
    @RequestMapping("/table")
    @PreAuthorize("hasAuthority('prod:category:page')")///权限
    public ResponseEntity<List<Category>>loadCategoryList(){
        List<Category> categories = categoryService.selectCategoryList();
        //响应
        return  ResponseEntity.ok(categories);
    }
    //==================查询所有商品的父级分类========================
    @RequestMapping("/listCategory")
    @PreAuthorize("hasAuthority('prod:category:page')")
    public ResponseEntity<List<Category>>loadParentCategoryList(){
        List<Category> categories = categoryService.selectParentCategoryList();
        return ResponseEntity.ok(categories);
    }
    //=======================添加商品分类===================================
    @PostMapping
    @PreAuthorize("hasAuthority('prod:category:save')")
    public ResponseEntity<String> addCategory(@RequestBody Category category){

        categoryService.saveCategory(category);

        return ResponseEntity.ok().build();
    }
}
