package com.lambda.usebean;

/**
 * 实体类Person
 *
 * @author MingChenchen
 */
public class Person {
    private String name;      //姓名
    private String location;  //地址

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Person:" + name + "," + location;
    }
}