package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

////此类为公共类存放减库存的商品id   和要减去的库存数信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdChange {
    //商品id
    private  Long prodId;
    //要减去的库存数
    private  Integer count;
}
