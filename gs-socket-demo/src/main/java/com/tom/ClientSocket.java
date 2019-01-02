package com.tom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 8888);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter.println("hello, tom");
            while (true) {
                String readLine = bufferedReader.readLine();
                if (null == readLine) {
                    break;
                }
                System.out.println("收到服务器端的消息：" + readLine);
            }
            printWriter.close();
            //bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
