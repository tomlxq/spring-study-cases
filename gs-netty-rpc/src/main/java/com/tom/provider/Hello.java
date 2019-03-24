package com.tom.provider;

import com.tom.api.IHello;

public class Hello implements IHello {
    @Override
    public String sayHello(String name) {
        return "hello," + name;
    }
}
