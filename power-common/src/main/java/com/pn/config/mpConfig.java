package com.pn.config;
//////mybatis-plus配置类

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@MapperScan(basePackages = "com.pn.dao")
@Configuration
public class mpConfig {


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
            MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();
               interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
                return   interceptor;
    }
}
