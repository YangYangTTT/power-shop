package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
//用于组装数据
public class CartMoney {

    //总金额
    private BigDecimal totalMoney = BigDecimal.ZERO;

    //可能还有其它金额,如折扣金额、运算金额
}
