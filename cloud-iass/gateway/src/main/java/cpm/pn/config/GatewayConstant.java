package cpm.pn.config;

import java.util.Arrays;
import java.util.List;

//网关常量类:
public interface GatewayConstant {

    //定义一个List<String>常量,里面存放网关全局过滤器直接放行的url地址
    public static final List<String> PASS_URLS = Arrays.asList("/oauth/token","/xx/xx","/xx/xx");

    //前端归还token的请求头的名称
    public static final String TOKEN_HEADER = "Authorization";

    //前端归还token的前缀
    public static final String TOKEN_PRE = "bearer ";
}
