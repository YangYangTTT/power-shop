package com.pn.controller;


import cn.hutool.core.io.FileUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;

//////图片上传controller
@RestController
public class FileController {
    //1:注入fastdfs   (用于操作文件系统fastdfs)
    @Autowired
      private FastFileStorageClient fastFileStorageClient;


    ///2将gitee中配置文件resource.url属性值注入给控制器 host属性，其值为fastdfs配置的nginx的url地址
    // （fastdfs访问上传成功的文件是通过其配置的nginx）
    @Value("${resources.url}")
    private  String host;

    @PostMapping("/admin/file/upload/element")
    public ResponseEntity<String> uploadFile(MultipartFile file){

           //3:获取文件名
        String originalFilename = file.getOriginalFilename();
        ///4:获取文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);

        ///4:上传文件（调用fastFileStorageClient里面的uploadFile方法  并上传四个参数 实现文件的上传）
        //参数1：上传文件的字节数据  参数2：上传文件的大小 参数3：上传的文件后缀 参数4：给个hashset
        //返回值为文件上传到文件系统fastdfs的存储节点storage的位置路径
        StorePath storePath=null;
        try {
             storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, new HashSet<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ///5：生成文件上传成功路径   fastdfs配置的nginx的访问地址  +文件上传到系统fastdfs的存储节点storage的路径位置
        String realPath= host+storePath.getFullPath();
        //:6：返回文件上传到文件系统fastdfs的访问路径；
        return  ResponseEntity.ok(realPath);

    }
}
