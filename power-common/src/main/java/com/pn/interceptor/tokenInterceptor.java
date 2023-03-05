package com.pn.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//拦截器   实现requestIntercepter
///1：客户端调用服务   从客户端的请求头中拿到token放在requestTemplate 中
///2：一个服务调用另一个服务   生成永久token放在 requestTemplate种
///3：第三方服务调用服务   也没有携带token  则生成一个永久token放在requestTemplate中
@Component
public class tokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        ////客户端请求时    （拿到请求头中的token）
        if(!ObjectUtils.isEmpty(requestAttributes)){
            String token = requestAttributes.getRequest().getHeader("Authorization");
            if(StringUtils.hasText(token)){
           requestTemplate.header("Authorization",token);
           return;
            }
        }
        ////服务调用服务   和   第三方调用服务
        requestTemplate.header("Authorization","yyyy");
    }
}
