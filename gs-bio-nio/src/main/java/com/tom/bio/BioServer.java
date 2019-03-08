package com.tom.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    static ServerSocket server = null;

    public BioServer(int port) {
        try {
            //sock服务启动
            server = new ServerSocket(port);
            System.out.println("服务端开始启动监听,监听端口：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listener() throws IOException {
        //死循环监听
        while (true) {
            //等待客户端联接
            final Socket client = server.accept();
            //拿到输入流，也就是乡村公路
            final InputStream inputStream = client.getInputStream();
            //缓冲区，byte数组
            final byte[] bytes = new byte[1024];
            final int len = inputStream.read(bytes);
            //只要一直有数据len就会大于0
            System.out.println("len:" + len);
            if (len != -1) {
                final String s = new String(bytes, 0, len);
                System.out.println("服务端读到的数据为" + s);
            }
            client.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new BioServer(8080).listener();


    }
}
