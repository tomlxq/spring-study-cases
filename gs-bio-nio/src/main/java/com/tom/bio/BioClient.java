package com.tom.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class BioClient {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        final long count = countDownLatch.getCount();
        for (int i = 0; i < count; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("执行线程" + Thread.currentThread());
                        countDownLatch.await();
                        //开启一条乡村公路
                        Socket client = new Socket("localhost", 8080);
                        //打开输出输通道
                        OutputStream outputStream = client.getOutputStream();
                        //产生一个随机串
                        String str = UUID.randomUUID().toString();
                        //发送给客户端
                        outputStream.write(str.getBytes());
                        outputStream.close();
                        client.close();
                    } catch (Exception e) {
                        // System.out.println(e);
                    } finally {

                    }
                }
            }.start();
            countDownLatch.countDown();
        }

        System.out.println("执行线程over");

    }
}
