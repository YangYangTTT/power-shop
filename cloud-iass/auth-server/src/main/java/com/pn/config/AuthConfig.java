package com.pn.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/////授权服务器配置类(非对称加密)  ----->在powercommon的resourcesconfig里面需要配置资源配置类
@Configuration
public class AuthConfig  extends AuthorizationServerConfigurerAdapter {

    ///1:指定token的存储方式   jwt的token
    ////配置jwt token的转换器

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        ///创建jwttoken转换器
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
////给  jwttoken转换器设置密匙
        ClassPathResource resource = new ClassPathResource("mmy-jwt.jks");
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(resource, "mmy123".toCharArray());
        KeyPair keyPair = keyFactory.getKeyPair("mmy-jwt");
        tokenConverter.setKeyPair(keyPair);

        return tokenConverter;
    }

    //////配置存储方式tokenstore的对象       jwttokenstore
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }

    //-------------------------------------------------配置第三方----------------------------------------
    //配置加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
 //配置第三方
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //前台系统和后台系统的前端项目作为第三方配置的第三方 --- 使用的授权模式都是密码模式
                .withClient("web")//第三方id -- 前端已经给定了就是web
                .secret(passwordEncoder().encode("web-secret"))//第三方密码 --- 前端已经给定了就是web-secret
                .scopes("all")
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(7200)//token的过期时间 -- 测试给定2小时
                .redirectUris("https://www.baidu.com")

                .and()

            //用于给服务调用之间生成token的 --- 授权模式使用的是客户端模式
                //用于给服务调用之间生成token的 --- 授权模式使用的是客户端模式
                .withClient("service")//第三方id -- 已经给定了就是service
                .secret(passwordEncoder().encode("service-secret"))//第三方密码 --- 已经给定了就是service-secret
                .scopes("all")
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds(Integer.MAX_VALUE)//token的过期时间给最大整数值,即表示永不过期
                .redirectUris("https://www.baidu.com");
    }

////-------------------------------------------------------------------------------------------------------------------------------------
    //给授权服务器暴露资源

    ///注入认证资源管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //1:暴露token的存储方式tokenstore的对象
        endpoints.tokenStore(tokenStore());
  ///2:暴露jwttoken转换器
        endpoints.accessTokenConverter(accessTokenConverter());
        ///3：暴露认证管理器     允许密码授权模式隐身登陆
        endpoints.authenticationManager(authenticationManager);
    }
}