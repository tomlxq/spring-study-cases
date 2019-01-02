package com.tom;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SayHello extends Remote {
    String sayHello(String name) throws RemoteException;
}
