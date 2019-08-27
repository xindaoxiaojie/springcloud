package com.xing.enumTest;

import org.junit.Test;

public class TestEnum {
    @Test
    public void test01(){
        for (ColorEnum color:ColorEnum.values()) {
            System.out.println(color+",ordinal"+color.ordinal()+",name:"+color.name());
        }
    }
    @Test
    public void test02(){
        String typename="firewall";
        TypeEnum typeEnum = TypeEnum.fromTypeName(typename);
        System.out.println(typeEnum.name()+","+typeEnum.ordinal());
    }
}
