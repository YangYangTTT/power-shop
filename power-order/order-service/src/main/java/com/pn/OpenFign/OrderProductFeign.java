package com.pn.OpenFign;


import com.pn.entily.ChangeStock;
import com.pn.entily.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//远程调用product-service服务
@FeignClient(value = "product-service")
public interface OrderProductFeign {
    /*
      product-service服务的url接口/prod/prod/getSkuByIds的请求处理方法的签名,
      根据skuIds查询所有Sku;

      参数@RequestBody List<Long> skuIds用于接收请求参数传递的skuids;
     */
    @RequestMapping("/prod/prod/getSkuByIds")
    public List<Sku> getSkuByIds(@RequestBody List<Long> skuIds);


    ///修改库存的方法
    @RequestMapping("/updateStock")
    public void updateStock(@RequestBody ChangeStock changeStock);
}
