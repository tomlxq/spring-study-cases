package com.tom;


import javax.xml.ws.Endpoint;

/**
 * http://localhost:8888/sayHello?wsdl
 */
public class BootStrap {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/sayHello", new SayHelloImpl());
        System.out.println("publish webservice success");
    }
}
