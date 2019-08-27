package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    /*
    * 指定过滤器的执行时机
    * "pre"指路由前执行过滤
    * "route" 在路由时调用
    * "post" 路由后调用
    * "error" 处理请求发生错误时调用
    * */
    @Override
    public String filterType() {
        return "pre";
    }
    /*
    * 过滤器的执行顺序
    * 数字越小越先执行
    * */
    @Override
    public int filterOrder() {
        return 0;
    }
    /*
    * 是否执行过滤器
    * */
    @Override
    public boolean shouldFilter() {
        return true;
    }
    /*
    * 过滤器的逻辑
    * */
    @Override
    public Object run() throws ZuulException {
        System.out.println("zuul过滤器...");
        //向header中添加鉴权令牌
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取header
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if(authorization!=null){
            requestContext.addZuulRequestHeader("Authorization",authorization);
        }
        return null;
    }
}
