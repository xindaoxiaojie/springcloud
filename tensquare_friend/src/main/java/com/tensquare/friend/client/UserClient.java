package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tensquare-user",fallback = UserClientImpl.class)//指定被调用的服务,服务不可调用时启动熔断器
public interface UserClient {
    @RequestMapping(value = "/user/incfans/{userid}/{x}",method = RequestMethod.POST)//指定地址映射
    public int incFanscount(@PathVariable("userid") String userid,@PathVariable("x") int x);
    @RequestMapping(value = "/user/incfollow/{userid}/{x}",method = RequestMethod.POST)
    public int incFollowcount(@PathVariable("userid") String userid,@PathVariable("x") int x);
}
