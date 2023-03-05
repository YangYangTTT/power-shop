package com.pn;


import com.pn.entily.Prod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


////value值为远程调用方法的服务名称     fallback为实现此接口的类    表示 备选方案 即远程调用失败 跳转的页面

@FeignClient(value = "product-service",fallback =  StoreProductFeignHystrix.class)
public interface StoreProductFeign {

    @RequestMapping("/getProdByProdId")
    public Prod getProdByProdId(@RequestParam("proId") Long proId);

}
