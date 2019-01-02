package com.tom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MulticastServer {
    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket();
            //224.0.0.0~239.255.25.255
            InetAddress inetAddress = InetAddress.getByName("224.1.1.2");
            for (int i = 0; i < 10; i++) {
                String hello = "hello tom" + i;
                byte[] bytes = hello.getBytes(Charset.defaultCharset());
                multicastSocket.send(new DatagramPacket(bytes, bytes.length, inetAddress, 8888));
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
