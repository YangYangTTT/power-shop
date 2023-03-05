package com.pn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.Notice;
import com.pn.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shop/notice")
@RestController
public class NoticeController {
    //注入INoticeService
    @Autowired
    private NoticeService noticeService;


    @GetMapping("/page")
    @PreAuthorize("hasAuthority('shop:notice:page')")
    public ResponseEntity<Page<Notice>> loadNoticePage(Page<Notice> page, Notice notice) {

        Page<Notice> noticePage = noticeService.findNoticePage(page, notice);

        return ResponseEntity.ok(noticePage);
    }
    ////------------------------------------------------新增公告--------------------------------------------------
    public ResponseEntity<Void>addNotice(@RequestBody Notice notice){
noticeService.save(notice);
return ResponseEntity.ok().build();
    }

}
