package com.pn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

////开启注解方式的缓存
@EnableCaching
//开启eureka客户端
@EnableEurekaClient
@SpringBootApplication
public class serviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(serviceApplication.class,args);
    }
}
