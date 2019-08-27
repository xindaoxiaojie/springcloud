package com.xing.enumTest;

public enum TypeEnum {
    FIREWALL("firewall"),
    SECRET("secretMac"),
    BALANCE("f5");
    private String typename;
    TypeEnum(String name){
        this.typename=name;
    }
    public static TypeEnum fromTypeName(String typename){
        for (TypeEnum typeEnum :TypeEnum.values()) {
            if(typeEnum.typename.equals(typename)){
                return typeEnum;
            }
        }
        return null;
    }
    public String getTypeName(){
        return this.typename;
    }
}
