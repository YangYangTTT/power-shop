package com.pn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//开启eureka客户端
@EnableEurekaClient
@SpringBootApplication
public class managerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(managerServiceApplication.class,args);
    }
}
