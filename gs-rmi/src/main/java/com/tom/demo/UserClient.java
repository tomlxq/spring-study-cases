package com.tom.demo;

import java.io.IOException;

public class UserClient {
    public static void main(String[] args) throws IOException {
        UserStub userStub = new UserStub();
        System.out.println(userStub.getAge());
    }
}
