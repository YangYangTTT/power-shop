package com.pn.service.impi;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.dao.EsProdDao;
import com.pn.entily.EsProd;
import com.pn.entily.Prod;
import com.pn.service.EsImportService;
import com.pn.service.ProdService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class EsImportServiceImpl implements EsImportService {
    //注入IProdService
    @Autowired
    private ProdService prodService;

    //注入EsProdDao
    @Autowired
    private EsProdDao esProdDao;

    //将配置文件esimport.size属性值赋值给size属性 -- 每页查询行数
    @Value("${esimport.size}")
    private Integer size;


    @PostConstruct//项目一启动就执行importall方法  实现数据的导入
    @Override
    public void importAll() {
        //查询prod表总行数
        Integer total = prodService.count();
        if(total>0){///如果总行数大于零  就计算总页数   有多少页就循环多少次
            int pags=total%size==0? total%size:((total%size)+1);
        for(int i=1;i<=pags;i++){     //pages为计算的总页数  所以要循环pages此 每次分页查询并将数据导入es
            int current=i;
            importToEs(current, size);
        }
        }
    }
    private  void   importToEs(int i,Integer size){
        //创建page对象 指定 页码和每页行数
        Page<Prod> page=new Page<>(i,size);
        //分页查询数据
        List<Prod> prods=prodService.selectProdPageForEs(page);//此方法需要自定义
        ////////将list<prod>转换为 list<espod>
                 List<EsProd> esProdList=new ArrayList<>();
                 for(Prod prod:prods){
                         EsProd esProd=new EsProd();
                     //将参数一Prod对象所有属性值赋值给参数二EsProd对象对应属性,参数三true忽略大小写
                     BeanUtil.copyProperties(prod,esProd,true);
                 }
                 ///向es索引中保存数据
         esProdDao.saveAll(esProdList);
    }
}
