package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_settlement")
public class OrderSettlement implements Serializable {
    /**
     * 支付结算单据ID
     */
    @TableId(value = "settlement_id", type = IdType.INPUT)
    private Long settlementId;

    /**
     * 支付单号
     */
    @TableField(value = "pay_no")
    private String payNo;

    /**
     * 外部订单流水号
     */
    @TableField(value = "biz_pay_no")
    private String bizPayNo;

    /**
     * order表中的订单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 支付方式 1 微信支付 2 支付宝
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 支付方式名称
     */
    @TableField(value = "pay_type_name")
    private String payTypeName;

    /**
     * 支付金额
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 是否清算 0:否 1:是
     */
    @TableField(value = "is_clearing")
    private Integer isClearing;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 清算时间
     */
    @TableField(value = "clearing_time")
    private Date clearingTime;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Integer version;

    /**
     * 支付状态
     */
    @TableField(value = "pay_status")
    private Integer payStatus;

    private static final long serialVersionUID = 1L;
}