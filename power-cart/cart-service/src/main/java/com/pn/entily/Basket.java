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
    * 购物车
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "basket")
public class Basket implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "basket_id", type = IdType.INPUT)
    private Long basketId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * SkuID
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 购物车产品个数
     */
    @TableField(value = "basket_count")
    private Integer basketCount;

    /**
     * 购物时间
     */
    @TableField(value = "basket_date")
    private Date basketDate;

    /**
     * 满减活动ID
     */
    @TableField(value = "discount_id")
    private Long discountId;

    /**
     * 分销推广人卡号
     */
    @TableField(value = "distribution_card_no")
    private String distributionCardNo;

    private static final long serialVersionUID = 1L;
}