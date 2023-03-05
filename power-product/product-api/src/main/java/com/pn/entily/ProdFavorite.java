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
    * 商品收藏表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "prod_favorite")
public class ProdFavorite implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "favorite_id", type = IdType.INPUT)
    private Long favoriteId;

    /**
     * 商品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 收藏时间
     */
    @TableField(value = "rec_time")
    private Date recTime;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    private static final long serialVersionUID = 1L;
}