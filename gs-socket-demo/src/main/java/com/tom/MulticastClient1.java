package com.tom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient1 {
    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(8888);
            //224.0.0.0~239.255.25.255
            InetAddress inetAddress = InetAddress.getByName("224.1.1.2");
            multicastSocket.joinGroup(inetAddress);
            byte[] buf = new byte[256];
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(datagramPacket);
                String s = new String(datagramPacket.getData());
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
