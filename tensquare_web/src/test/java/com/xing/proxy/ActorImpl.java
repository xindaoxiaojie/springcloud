package com.xing.proxy;

public class ActorImpl implements Actor {
    @Override
    public void m1() {
        System.out.println("m1执行");
    }

    @Override
    public void m2() {
        System.out.println("m2执行");
    }
}
