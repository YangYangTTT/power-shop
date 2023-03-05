package cpm.pn;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;


//开启eureka客户端
@EnableEurekaClient
@SpringBootApplication
public class gatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(gatewayApplication.class,args);
    }

}
