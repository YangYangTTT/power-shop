package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//此类存放减库存的skuid   以及要减去的库存数
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuChange {
    //skuid
    private  Long skuId;
   //要减去的skus库存数
    private  Integer count;
}
