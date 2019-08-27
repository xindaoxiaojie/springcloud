package com.tensquare.friend.client;

import entity.Result;
import org.springframework.stereotype.Component;
/*
* 指定Feign客户端界面的后备类。回退类必须是有效的spring bean
* */
@Component
public class UserClientImpl implements UserClient {
    @Override
    public int incFanscount(String userid, int x) {
        //throw new RuntimeException("熔断器执行了");
        //System.out.println("incFanscount的熔断器执行了");
        return 0;
    }

    @Override
    public int incFollowcount(String userid, int x) {
        //throw new RuntimeException("熔断器执行了");
        //System.out.println("incFollowcount的熔断器执行了");
        return 0;
    }
}
