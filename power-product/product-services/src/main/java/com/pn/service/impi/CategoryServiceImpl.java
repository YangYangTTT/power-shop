package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.Category;
import com.pn.dao.CategoryMapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@CacheConfig(cacheNames = "com.pn.service.impi.CategoryServiceImpl")
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    ////注入CategoryMapper
    @Autowired
    private  CategoryMapper categoryMapper;


    @Cacheable(key="'category:all'")
    @Override
    public List<Category> selectCategoryList() {
      /////查询所有商品分类并按seq降序排列
        return  categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getSeq)
        );
    }
//===========================查询所有商品父级分类（新增产品分类时查询）=============================
    @Cacheable(key="'category:paraent'")
    @Override
    public List<Category> selectParentCategoryList() {
////查询所有parent_id列的值为0的商品分类即为父分类，  并且status值为1 为可用父分类，且按照seq列降序
      return   categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId,0)
                .eq(Category::getStatus,1)
                .orderByDesc(Category::getSeq)
        );
    }
//================================添加商品分类=========================
///将以前查询的所有商品分类 以及商品父级分类缓存全部清除    因为有新增的商品分类
@Caching(evict = {
        @CacheEvict(key = "'category:all'"),
        @CacheEvict(key = "'category:parent'")
})
    @Override
    public boolean saveCategory(Category category) {
        //拿到分类id的父分类id
    Long parentId = category.getParentId();
    if(parentId==null){///不存在说明新增分类时没有选
        category.setParentId(0L);//父级分类id
        category.setGrade(1);///分类等级
    }else{///存在父级分类id
        category.setGrade(2);//设置新增分类等级为2
    }
    //设置创建时间
    category.setRecTime(new Date());
    //shop_id设置为1
    category.setShopId(1L);
    return  categoryMapper.insert(category)>0;
}
}
