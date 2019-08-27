package com.xing.proxy;

public class Proxy implements Actor {
    private ActorImpl actorimpl=new ActorImpl();
    @Override
    public void m1() {
        System.out.println("111111111111111");
        actorimpl.m1();
        System.out.println("222222222222222");
    }

    @Override
    public void m2() {
        System.out.println("333333333333333");
        actorimpl.m2();
        System.out.println("444444444444444");
    }
}
