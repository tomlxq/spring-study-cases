package com.tom;

public class CalGroupId {
    public static void main(String[] args) {
        System.out.println(Math.abs("DemoGroup1".hashCode()) % 50);
    }
}
