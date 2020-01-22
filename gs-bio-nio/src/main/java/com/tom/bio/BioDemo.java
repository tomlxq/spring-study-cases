package com.tom.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.LongStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/11
 */
@Slf4j
public class BioDemo {

    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // server();
        // 启动100个线程连客户端
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        long count = countDownLatch.getCount();
        LongStream.range(0, count).forEach(idx -> {
            new Thread(() -> {
                try {
                    log.info("当前线程为：{} 线程名称为：{}", idx, Thread.currentThread().getName());
                    countDownLatch.await();
                    try (
                        // 建立与服务器端的连接，如果服务器没启动，报Connection refused异常
                        Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
                        // 打开输出输通道
                        OutputStream outputStream = socket.getOutputStream();
                        // 接收服务端的消息
                        InputStream inputStream = socket.getInputStream()) {
                        // 产生一个随机串, 发送给客户端
                        outputStream.write(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
                        outputStream.flush();
                        byte[] bytes = new byte[1024];
                        int len = inputStream.read(bytes);
                        if (len != -1) {
                            log.info("收到服务器的消息：{}", new String(bytes, 0, len));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            countDownLatch.countDown();
        });

    }

    private static void server() {
        // 开启一个线程启动服务器监听
        new Thread(() -> {
            try {
                // 服务器启动，开始监听8080端口
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), PORT));
                log.info("服务端开始启动监听,监听端口：" + PORT);
                while (true) {
                    try (
                        // 阻塞式等待客户端的连接，有连接才返回Socket对象
                        Socket accept = serverSocket.accept();
                        // 获取客户端发过来的信息流
                        InputStream inputStream = accept.getInputStream()) {

                        // 缓冲区，数组而已
                        byte[] bytes = new byte[1024];
                        // 只要一直有数据写入，len就会一直大于0
                        int len = inputStream.read(bytes);
                        if (len > 0) {
                            System.out.println("收到客户端的消息：" + new String(bytes, 0, len));
                        }
                        // 获取输出流对象，从而写入数据返回客户端
                        OutputStream outputStream = accept.getOutputStream();
                        outputStream.write(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
