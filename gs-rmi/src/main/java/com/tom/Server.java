package com.tom;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Hello world!
 */
public class Server {
    public static void main(String[] args) {
        try {
            SayHello sayHello = new SayHelloImpl();
            LocateRegistry.createRegistry(8889);
            Naming.bind("rmi://localhost:8889/sayHello", sayHello);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.out.println("服务端success");

    }
}
