package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 商品
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "prod")
public class Prod implements Serializable {
    /**
     * 产品ID
     */
    @TableId(value = "prod_id", type = IdType.INPUT)
    private Long prodId;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    private String prodName;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    @TableField(value = "brief")
    private String brief;

    /**
     * 详细描述
     */
    @TableField(value = "content")
    private String content;

    /**
     * 商品主图
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 商品图片，以,分割
     */
    @TableField(value = "imgs")
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 商品分类
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 销量
     */
    @TableField(value = "sold_num")
    private Integer soldNum;

    /**
     * 总库存
     */
    @TableField(value = "total_stocks")
    private Integer totalStocks;

    /**
     * 配送方式json见TransportModeVO
     */
    @TableField(value = "delivery_mode")
    private String deliveryMode;

    /**
     * 运费模板id
     */
    @TableField(value = "delivery_template_id")
    private Long deliveryTemplateId;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 上架时间
     */
    @TableField(value = "putaway_time")
    private Date putawayTime;

    /**
     * 版本 乐观锁
     */
    @TableField(value = "version")
    private Integer version;

    private static final long serialVersionUID = 1L;

    //接收所有商品标签id
    @TableField(exist = false)
    private List<Long> tagList;

    //接收所有sku信息
    @TableField(exist = false)
    private List<Sku> skuList;


    //接口配送方式
    @TableField(exist = false)
    private DeliveryModeVo deliveryModeVo;


    /*
    静态内部类DeliveryModeVo,描述配送方式,hasShopDelivery属性
    值为true表示商家配送,hasUserPickup属性值为true表示用户自提;
   */
    @Data
    public static class DeliveryModeVo {

        @TableField(exist = false)
        private Boolean hasShopDelivery;//为true  商家配送

        @TableField(exist = false)
        private Boolean hasUserPickup;///为true  自提
    }
    /*
     Prod类追加商品好评数和商品好评率两个属性,目的是和EsProd类
     的属性保持一致,因为从mysql查询商品信息是先存到Prod对象再转
     存到EsProd对象的;
    */
    //商品好评数
    @TableField(exist = false)
    private Long praiseNumber = 0L;

    //商品好评率
    @TableField(exist = false)
    private BigDecimal positiveRating = BigDecimal.ZERO;
}