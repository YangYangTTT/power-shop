package com.pn;

import cn.hutool.core.lang.Snowflake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

//eureka客户端
@EnableEurekaClient
//openfign客户端
@EnableFeignClients
@SpringBootApplication
public class orderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(orderServiceApplication.class,args);
    }

    //配置hutool工具包提供的snowflake的ben对象   使用其雪花算法生成订单号 目的是使生成的订单号不重复
    @Bean
    public Snowflake snowflake(){
        //构造器参数给默认值0,0
        return new Snowflake(0, 0);
    }
}
