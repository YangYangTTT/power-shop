package com.pn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.IndexImg;
import com.pn.service.IndexImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/indexImg")
@RestController
public class IndexImgController {


    @Autowired
    private IndexImgService indexImgService; //注入IIndexImgService

    ///-------------------------------------分页查询轮播图--------------------------------------
    @RequestMapping("/page")
    @PreAuthorize("hasAuthority('admin:indexImg:page')")
    public ResponseEntity<Page<IndexImg>> loadIndexImgPage(Page<IndexImg> page, IndexImg indexImg) {
        Page<IndexImg> indexImgPage = indexImgService.findIndexImgPage(page, indexImg);

        return ResponseEntity.ok(indexImgPage);
    }
    //---------------------------------保存轮播图----------------------------------------------------
    @PostMapping
    @PreAuthorize("hasAuthority('admin:indexImg:save')")
    public ResponseEntity<Void> addIndexImg(@RequestBody IndexImg indexImg) {

        indexImgService.save(indexImg);

        return ResponseEntity.ok().build();
    }
    ///--------------------------------------------远程服务调用-----------------------------------
      /*
      url接口/admin/indexImg/info/{id}的请求处理方法:
      @PathVariable("id") Long id将占位符id的值赋值给方法参数id;

      同时设置具有admin:indexImg:info权限的用户可访问该url接口;
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasAuthority('admin:indexImg:info')")
    public ResponseEntity<IndexImg> loadIndexImgById(@PathVariable("id") Long id) {

        IndexImg indexImg = indexImgService.getById(id);

        return ResponseEntity.ok(indexImg);
    }

    ////===============================查询轮播图==========================
    //url接口/admin/indexImg/indexImgs的请求处理方法
    @GetMapping("/indexImgs")
    public ResponseEntity<List<IndexImg>> loadFrontIndexImg() {
        //执行业务方法
        List<IndexImg> indexImgs = indexImgService.list();

        return ResponseEntity.ok(indexImgs);
    }
    }
}
