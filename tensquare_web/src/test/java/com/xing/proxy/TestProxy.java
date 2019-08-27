package com.xing.proxy;

import org.junit.Test;

public class TestProxy {
    @Test
    public void test01(){
        Actor actor = new ActorImpl();
        actor.m1();
        actor.m2();
        Actor proxy = new Proxy();
        proxy.m1();
        proxy.m2();
    }
}
