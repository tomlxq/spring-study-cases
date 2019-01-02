package com.tom;


import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Hello world!
 */
public class ServerSocket {
    public static void main(String[] args) throws IOException {
        java.net.ServerSocket serverSocket = null;
        try {
            serverSocket = new java.net.ServerSocket(8888);
            while (true) {
                Socket accept = serverSocket.accept();
                new Thread(() -> {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(accept.getOutputStream()), true);
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            System.out.println("服务端收到消息：" + readLine);
                            printWriter.println("hello client:^^");
                            printWriter.flush();
                        }
                        IOUtils.closeQuietly(bufferedReader);
                        IOUtils.closeQuietly(printWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } finally {
            if (null != serverSocket) {
                serverSocket.close();
            }
        }
    }
}
