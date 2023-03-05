package com.pn.service;

import com.pn.entily.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category>{

    ///点击分类管理  查询所有商品 分类
    public List<Category> selectCategoryList();

    ///查询所有商品父级分类 （新增商品分类时先查询）
    public List<Category>selectParentCategoryList();

    ////添加商品分类
    public  boolean saveCategory(Category category);
}
