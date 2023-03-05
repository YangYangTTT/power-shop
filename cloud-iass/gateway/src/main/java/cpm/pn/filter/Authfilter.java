package cpm.pn.filter;


import com.ctc.wstx.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cpm.pn.config.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

//////全局过滤器  判断请求是否为认证请求     是  放行     不是则对比token  与redis里面一致则放行   不一致 向客户端返回错误信息
@Component
public class Authfilter implements GlobalFilter, Ordered {

    //注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //拿到请求的url地址
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();


        //1.如果是认证请求则直接放行   GatewayConstant.PASS_URLS里面存放的是网关全局过滤器直接放行的地址
        ///现在是放的是/oauth/token
        if(GatewayConstant.PASS_URLS.contains(path)){
            return chain.filter(exchange);
        }


        //2.如果不是认证请求,是其它资源请求,但是携带了token,并且token校验通过,则也放行
        List<String> tokenHeaders = request.getHeaders().get(GatewayConstant.TOKEN_HEADER);
        if(!CollectionUtils.isEmpty(tokenHeaders)){
            String tokenHeader = tokenHeaders.get(0);
            if(StringUtils.hasText(tokenHeader)){
                String token = tokenHeader.replaceAll(GatewayConstant.TOKEN_PRE, "");
                if(StringUtils.hasText(token)&&redisTemplate.hasKey(token)){
                    return chain.filter(exchange);
                }
            }
        }
       /////////3:不是认证请求  并且没有携带token以及token效验失败      直接响应401
        ServerHttpResponse response = exchange.getResponse();
       response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",401);
       map.put("msg","尚未认可");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(map);//////将map里的信息转换为byte类型
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
