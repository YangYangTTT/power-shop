package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 热搜
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "hot_search")
public class HotSearch implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "hot_search_id", type = IdType.INPUT)
    private Long hotSearchId;

    /**
     * 店铺ID 0为全局热搜
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 录入时间
     */
    @TableField(value = "rec_date")
    private Date recDate;

    /**
     * 顺序
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * 状态 0下线 1上线
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 热搜标题
     */
    @TableField(value = "title")
    private String title;

    private static final long serialVersionUID = 1L;
}