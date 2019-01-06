package com.tom;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SayHelloImplService sayHelloImplService = new SayHelloImplService();
        SayHelloImpl sayHelloImplPort = sayHelloImplService.getSayHelloImplPort();

        String msg = sayHelloImplPort.sayHello("tom");
        System.out.println(msg);


    }
}
