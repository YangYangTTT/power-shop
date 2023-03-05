package com.pn;

import com.pn.entily.Prod;
import org.springframework.stereotype.Component;

@Component
public class StoreProductFeignHystrix  implements StoreProductFeign{


    //////备选方案
    @Override
    public Prod getProdByProdId(Long proId) {
       return null;
    }
}
