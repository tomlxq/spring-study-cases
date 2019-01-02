package com.tom;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SayHelloImpl extends UnicastRemoteObject implements SayHello {
    protected SayHelloImpl() throws RemoteException {
    }

    @Override
    public String sayHello(String name) {
        return "name = [" + name + "]";
    }
}
