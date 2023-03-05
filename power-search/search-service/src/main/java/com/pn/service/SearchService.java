package com.pn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.EsProd;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {
    /*
      根据商品标签分组id从ES中分页查询该分组下的商品的业务方法;
      参数一 Long tagId：商品标签分组id
      参数二 Integer current：页码
      参数三 Integer size：每页行数
      返回值Page<EsProd>：将分页查询结果组装到Page对象返回
     */
    public Page<EsProd> searchProdByTagId(Long tagId, Integer current, Integer size);

 /*
      根据商品名称从ES中分页查询商品的业务方法;
      参数一 String prodName：商品名称
      参数二 Integer current：页码
      参数三 Integer size：每页行数
      参数四 Integer sort：排序方式
      返回值Page<EsProd>：将分页查询结果组装到Page对象返回
     */
    public  Page<EsProd> searchProdByProdName(String prodName, Integer current,
                                              Integer size, Integer sort);
}
