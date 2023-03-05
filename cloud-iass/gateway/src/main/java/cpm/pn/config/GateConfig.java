package cpm.pn.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/////后置过滤器    用于将监控中心认证授权的token返回的token存入redis  并返回给客户端
@Configuration
public class GateConfig {

    //注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
      给认证授权服务配置个路由:
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){

        RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes();
        //R apply(T t, U u)
        return builder.route("auth-server-id",
                t -> t.path("/oauth/token")//url接口断言
                        .filters(f -> f.modifyResponseBody(String.class, String.class, ((exchange, jsonStr) -> {
                            JSONObject jsonObj = JSON.parseObject(jsonStr);
                            if(jsonObj.containsKey("access_token")){//认证通过,token颁发了
                                String token = jsonObj.getString("access_token");//token
                                Long expires = jsonObj.getLong("expires_in");//token的过期时间
                                redisTemplate.opsForValue().set(token, "", expires, TimeUnit.SECONDS);
                            }
                            return Mono.just(jsonStr);
                        })))//给路由配置局部过滤器
                        .uri("lb://auth-server")//路由到的认证授权服务的地址
        ).build();
    }
}