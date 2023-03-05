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
@TableName(value = "order_refund")
public class OrderRefund implements Serializable {
    /**
     * 记录ID
     */
    @TableId(value = "refund_id", type = IdType.INPUT)
    private Long refundId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单流水号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 订单总金额
     */
    @TableField(value = "order_amount")
    private Double orderAmount;

    /**
     * 订单项ID 全部退款是0
     */
    @TableField(value = "order_item_id")
    private Long orderItemId;

    /**
     * 退款编号
     */
    @TableField(value = "refund_sn")
    private String refundSn;

    /**
     * 订单支付流水号
     */
    @TableField(value = "flow_trade_no")
    private String flowTradeNo;

    /**
     * 第三方退款单号(微信退款单号)
     */
    @TableField(value = "out_refund_no")
    private String outRefundNo;

    /**
     * 订单支付方式 1 微信支付 2 支付宝
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 订单支付名称
     */
    @TableField(value = "pay_type_name")
    private String payTypeName;

    /**
     * 买家ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 退货数量
     */
    @TableField(value = "goods_num")
    private Integer goodsNum;

    /**
     * 退款金额
     */
    @TableField(value = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 申请类型:1,仅退款,2退款退货
     */
    @TableField(value = "apply_type")
    private Integer applyType;

    /**
     * 处理状态:1为待审核,2为同意,3为不同意
     */
    @TableField(value = "refund_sts")
    private Integer refundSts;

    /**
     * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
     */
    @TableField(value = "return_money_sts")
    private Integer returnMoneySts;

    /**
     * 申请时间
     */
    @TableField(value = "apply_time")
    private Date applyTime;

    /**
     * 卖家处理时间
     */
    @TableField(value = "handel_time")
    private Date handelTime;

    /**
     * 退款时间
     */
    @TableField(value = "refund_time")
    private Date refundTime;

    /**
     * 文件凭证json
     */
    @TableField(value = "photo_files")
    private String photoFiles;

    /**
     * 申请原因
     */
    @TableField(value = "buyer_msg")
    private String buyerMsg;

    /**
     * 卖家备注
     */
    @TableField(value = "seller_msg")
    private String sellerMsg;

    /**
     * 物流公司名称
     */
    @TableField(value = "express_name")
    private String expressName;

    /**
     * 物流单号
     */
    @TableField(value = "express_no")
    private String expressNo;

    /**
     * 发货时间
     */
    @TableField(value = "ship_time")
    private Date shipTime;

    /**
     * 收货时间
     */
    @TableField(value = "receive_time")
    private Date receiveTime;

    /**
     * 收货备注
     */
    @TableField(value = "receive_message")
    private String receiveMessage;

    private static final long serialVersionUID = 1L;
}