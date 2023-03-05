package com.pn.entily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdCommData {

    //评论总数
    private Integer number;

    //好评数
    private Long praiseNumber;

    //好评率
    private BigDecimal positiveRating;

    //中评数
    private Integer secondaryNumber;

    //差评数
    private Integer negativeNumber;

    //带图评论数
    private Integer picNumber;
}