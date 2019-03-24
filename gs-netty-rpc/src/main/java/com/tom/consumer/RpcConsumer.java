package com.tom.consumer;

import com.tom.api.ICal;
import com.tom.api.IHello;
import com.tom.consumer.proxy.RpcProxy;

public class RpcConsumer {
    public static void main(String[] args) {
        //本地可以这样写
        //IHello hello=new Hello();
        //远程调用，采用伪代理，实质上是发起远程请求
        IHello hello = new RpcProxy().create(IHello.class);
        String msg = hello.sayHello("tom");
        System.out.println(msg);
        int a = 5, b = 2;
        ICal cal = new RpcProxy().create(ICal.class);
        System.out.println(a + " + " + b + " = " + cal.add(a, b));
        System.out.println(a + " - " + b + " = " + cal.sub(a, b));
        System.out.println(a + " * " + b + " = " + cal.multi(a, b));
        System.out.println(a + " / " + b + " = " + cal.div(a, b));
    }
}
