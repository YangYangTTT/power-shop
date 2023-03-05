package com.pn;


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//注册eureka客户端
@EnableEurekaClient
///标记为admin监控的服务端
@EnableAdminServer
@SpringBootApplication
public class adminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(adminServerApplication.class,args);
    }
}
