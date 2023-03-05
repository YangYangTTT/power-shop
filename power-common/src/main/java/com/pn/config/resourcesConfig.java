package com.pn.config;


/////资源配置类     (创建jwt的token转换器               指定token的存储方式     配置资源服务的请求（取消跨站跨域请求等）    )
import cn.hutool.core.io.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.Charset;

////标记文资源服务器
@EnableResourceServer
///开启方法级别的授权
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class resourcesConfig  extends ResourceServerConfigurerAdapter {
      ////向ioc容器中配置表jwt的toke转换器(1:创建token转换器2：拿到存放钥匙文件3：读取他4设置token转换器公匙)
        @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
///创建jwt 的token转换器
            JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
             ///拿到存放公匙的文件
            ClassPathResource classPathResource=new ClassPathResource("公钥.txt");
            //读取公匙
            String publicKey = "";
            try {
             publicKey=  FileUtil.readString(classPathResource.getFile(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ////给jwt的token转换器设置公匙
            converter.setVerifierKey(publicKey);
            return converter;
        }
//-----------------------------------------------指定token存储方式-------------------------------
    @Bean
    public TokenStore tokenStore(){
            return  new JwtTokenStore(jwtAccessTokenConverter());
    }

    //告诉服务器通过jwt解析token
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }
    //-------------------------------------------------------------------------------------------------
//配置资源服务器的请求
    @Override
    public void configure(HttpSecurity http) throws Exception {
            //取消跨域  跨站  session检查
        http.csrf().disable();
        http.cors().disable();
   //  http.sessionManagement().disable();
     /////对actuator的所有监控端点进行放行
        http.authorizeRequests().mvcMatchers("/actuator/**").permitAll();
        /////其他资源必须通过认证才能访问
        http.authorizeRequests().anyRequest().authenticated();

    }
}
