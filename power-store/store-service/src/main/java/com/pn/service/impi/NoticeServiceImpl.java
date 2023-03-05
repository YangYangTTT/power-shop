package com.pn.service.impi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.Notice;
import com.pn.dao.NoticeMapper;
import com.pn.service.NoticeService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{
    //注入NoticeMapper
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public Page<Notice> findNoticePage(Page<Notice> page, Notice notice) {
        ////分页查询公告
          /*
         如果参数Notice对象的status、isTop、title属性值不为空,即是按照状态、是否置顶、公告
         内容搜索公告,则按照status列、isTop、title列模糊并分页查询出公告,然后按照publish_time
         列降序排序;

         如果参数Notice对象的status、isTop、title属性值为空,即是菜单公告管理查询公告,则直接
         分页查询出公告,然后再按publish_time列降序排序;
        */

        page=noticeMapper.selectPage(page,new LambdaQueryWrapper<Notice>()
                .eq(!ObjectUtils.isEmpty(notice.getStatus()),Notice::getStatus,notice.getStatus())
                .eq(!ObjectUtils.isEmpty(notice.getIsTop()),Notice::getIsTop,notice.getIsTop())
                .like(StringUtils.hasText(notice.getTitle()),Notice::getTitle,notice.getTitle())
                .orderByDesc(Notice::getPublishTime)
        );
        return page;
    }
    ///-------------------------------------保存公告------------------------------------------------------------------
    //重写save()方法
    @Override
    public boolean save(Notice notice) {
        //设置shop_id列为1
        notice.setShopId(1L);
        //设置更新时间为系统当前时间
        notice.setUpdateTime(new Date());
        //如果status属性值为true即状态是公布,则设置发布时间为系统当前时间
        if (notice.getStatus()) {
            notice.setPublishTime(new Date());
        }
        //添加
        return noticeMapper.insert(notice) > 0;
    }
}

