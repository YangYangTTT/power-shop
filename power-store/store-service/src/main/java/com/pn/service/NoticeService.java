package com.pn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
public interface NoticeService extends IService<Notice>{


//分页查询公告的方法
public Page<Notice> findNoticePage(Page<Notice> page, Notice notice);

}
