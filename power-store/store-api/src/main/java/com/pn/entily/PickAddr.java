package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 用户配送地址
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "pick_addr")
public class PickAddr implements Serializable {
    /**
     * ID
     */
    @TableId(value = "addr_id", type = IdType.INPUT)
    private Long addrId;

    /**
     * 自提点名称
     */
    @TableField(value = "addr_name")
    private String addrName;

    /**
     * 地址
     */
    @TableField(value = "addr")
    private String addr;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 省份ID
     */
    @TableField(value = "province_id")
    private Long provinceId;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市ID
     */
    @TableField(value = "city_id")
    private Long cityId;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 区/县ID
     */
    @TableField(value = "area_id")
    private Long areaId;

    /**
     * 区/县
     */
    @TableField(value = "area")
    private String area;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    private Long shopId;

    private static final long serialVersionUID = 1L;
}