package com.pn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//标记为eureka客户端
@EnableEurekaClient
@SpringBootApplication
public class searchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(searchServiceApplication.class,args);
    }
}
