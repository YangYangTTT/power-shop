package com.pn;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;
//添加mapper扫描包路径
@MapperScan("com.pn.dao")
//标记为eureka客户端
@EnableEurekaClient
//标记为授权中心
@EnableAuthorizationServer
@SpringBootApplication
public class authserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(authserverApplication.class,args);
    }

    /////配置resttemplate对象到ioc容器---使用其发送请求
    @Bean
    public  RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}
