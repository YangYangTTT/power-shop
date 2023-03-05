package com.pn.OpenFign;


import com.pn.entily.Basket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cart-service")
public interface OrderCartFeign {
    @PostMapping("/p/shopCart/getBasketByIds")
    public List<Basket> getBasketByIds(@RequestBody List<Long> basketIds);

    //cart-service服务的url接口/p/shopCart/deleteByUserIdAndSkuIds的请求处理方法的签名
    @RequestMapping("/p/shopCart/deleteByUserIdAndSkuIds")
    public void deleteByUserIdAndSkuIds(@RequestParam String userId,
                                        @RequestBody List<Long> skuIds);
}
