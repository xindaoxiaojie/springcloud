package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("过滤器执行-----------");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if (request.getMethod().equals("OPTIONS")){
            return null;
        }
        String url = request.getRequestURL().toString();
        if(url.indexOf("/admin/login")>0){
            System.out.println("登录页面"+url);
            return null;
        }
        String authorization = request.getHeader("Authorization");
        if(authorization!=null&&authorization.startsWith("Bearer ")){
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if(claims!=null){
                if("admin".equals(claims.get("roles"))){
                    requestContext.addZuulRequestHeader("Authorization",authorization);
                    System.out.println("验证通过并添加了头信息");
                    return null;
                }
            }
        }
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");
        return null;
    }
}
