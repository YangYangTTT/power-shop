package com.pn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//openfign客户端
@EnableFeignClients
//eureka客户端
@EnableEurekaClient
@SpringBootApplication
public class cartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(cartServiceApplication.class,args);
    }
}
