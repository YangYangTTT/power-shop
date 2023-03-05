package com.pn.entily;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//存放  ProdChange和SkuChange 的类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStock {
    //组装所有的ProdChange
    private List<ProdChange> ProdChanges;
    //组装所有的SkuChange
    private  List<SkuChange> skuChanges;

}
