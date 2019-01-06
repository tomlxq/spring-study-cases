package com.tom;

import javax.jws.WebService;

@WebService
public class SayHelloImpl implements SayHello {
    @Override
    public String sayHello(String name) {
        String msg = "Hi," + name;
        System.out.println(msg);
        return msg;
    }
}
