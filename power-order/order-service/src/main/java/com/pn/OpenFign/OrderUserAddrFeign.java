package com.pn.OpenFign;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.entily.UserAddr;
import lombok.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


//远程调用member-service服务
@FeignClient(value="number-service")
public interface  OrderUserAddrFeign {
    @GetMapping("/p/address/userDefaultAddr")
    public UserAddr getUserDefaultAddr(@RequestParam("userId") String userId);
}
