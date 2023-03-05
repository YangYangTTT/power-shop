package com.pn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.IndexImg;
import com.baomidou.mybatisplus.extension.service.IService;
public interface IndexImgService extends IService<IndexImg>{



    /////查询轮播图的方法
    public Page<IndexImg>   findIndexImgPage(Page<IndexImg> page,IndexImg indexImg);

}
