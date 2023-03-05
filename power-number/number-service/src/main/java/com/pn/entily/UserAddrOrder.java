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
    * 用户订单配送地址
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_addr_order")
public class UserAddrOrder implements Serializable {
    /**
     * ID
     */
    @TableId(value = "addr_order_id", type = IdType.INPUT)
    private Long addrOrderId;

    /**
     * 地址ID
     */
    @TableField(value = "addr_id")
    private Long addrId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 收货人
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 省ID
     */
    @TableField(value = "province_id")
    private Long provinceId;

    /**
     * 省
     */
    @TableField(value = "province")
    private String province;

    /**
     * 区域ID
     */
    @TableField(value = "area_id")
    private Long areaId;

    /**
     * 区
     */
    @TableField(value = "area")
    private String area;

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
     * 地址
     */
    @TableField(value = "addr")
    private String addr;

    /**
     * 邮编
     */
    @TableField(value = "post_code")
    private String postCode;

    /**
     * 手机
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 建立时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Integer version;

    private static final long serialVersionUID = 1L;
}