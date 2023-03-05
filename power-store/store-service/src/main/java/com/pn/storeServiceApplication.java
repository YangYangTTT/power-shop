package com.pn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

///开启eureka客户端
@EnableEurekaClient
////开启openfegin
@EnableFeignClients
//开启注解缓存
@EnableCaching

@SpringBootApplication
public class storeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(storeServiceApplication.class,args);
    }
}
