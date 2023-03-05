package com.pn;


import com.pn.entily.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//服务调用接口
@FeignClient("product-service")
public interface  CartProductFeign {
    //远程调用接口/prod/prod/getSkuByIds的请求处理方法的签名
    @RequestMapping("/prod/prod/getSkuByIds")
    public List<Sku> getSkuByIds(@RequestBody List<Long> skuIds);
}
