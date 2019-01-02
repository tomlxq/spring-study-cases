package com.tom.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UserSkeleton extends Thread {
    UserServer userServer;

    public UserSkeleton(UserServer userServer) {
        this.userServer = userServer;
    }


    @Override
    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            while (null != socket) {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String method = (String) objectInputStream.readObject();
                if ("age".equals(method)) {
                    int age = userServer.getAge();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeInt(age);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                }
                objectInputStream.close();

            }

        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
