package com.tom.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/12
 */
@Slf4j
public class NioDemo {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> {
            try { // 获取ServerSocketChannel实例
                ServerSocketChannel ssc = ServerSocketChannel.open();
                // 监听端口
                ssc.bind(new InetSocketAddress(InetAddress.getLocalHost(), PORT));
                // 设置channel为非阻塞模式
                ssc.configureBlocking(false);

                // 打开Selector，获取selector实例
                Selector selector = Selector.open();
                // 向selector注册channel和感兴趣的事件
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                log.info("服务器开始监听{}，等待链接", PORT);
                // 环以保证正常情况下服务器端一直处于运行状态
                while (true) {
                    // 获取selector实例中需要处理的SelectionKey的数量
                    int select = selector.select();// 阻塞等待就绪的Channel
                    log.info("获取selector实例中需要处理的SelectionKey的数量 {}", select);
                    // 遍历selector.selectedKeys,以对每个SelectionKey的事件进行处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        try {
                            if (key.isAcceptable()) {
                                // 当SelectionKey的类型是OP_ACCEPT时，获取绑定的ServerSocketChannel对象
                                ServerSocketChannel server = (ServerSocketChannel)key.channel();
                                // 接受客户端建立连接的请求，并返回SocketChannel对象
                                SocketChannel sc = server.accept();
                                sc.configureBlocking(false);
                                // 向Selector注册感兴趣的事件类型，如read,write
                                sc.register(selector, SelectionKey.OP_READ);
                            } else if (key.isReadable()) {
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                SocketChannel sc = (SocketChannel)key.channel();
                                // 从SocketChannel中读取数到ByteBuffer中
                                int len = sc.read(byteBuffer);
                                if (len <= 0) {
                                    break;
                                }
                                byteBuffer.flip();
                                log.info("收到客户端消息:{}", new String(byteBuffer.array(), 0, len));
                            } else if (key.isWritable()) {
                                SocketChannel sc = (SocketChannel)key.channel();
                                // 向SocketChannel中写入ByteBuffer对象数据
                                ByteBuffer encode = StandardCharsets.UTF_8.encode("你好，世界");
                                log.info("发送消息到客户端:{}", encode);
                                sc.write(encode);
                                sc.finishConnect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        it.remove();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        singleThreadPool.shutdown();

        // client();

    }

    private static void client() {
        try {
            // 打开SocketChannel
            final SocketChannel client = SocketChannel.open();
            // 将SocketChannel配置为非阻塞模式
            client.configureBlocking(false);
            // 连接到指定的目标地址
            client.connect(new InetSocketAddress(InetAddress.getLocalHost(), PORT));
            // 打开Selector，获取selector实例
            Selector selector = Selector.open();
            // 向Selector注册感兴趣的事件,connected,read,write
            client.register(selector, SelectionKey.OP_CONNECT);
            // 循环执行保证客户端一直处于运行状态
            while (true) {
                // 从Selector中获取是否有可读的key信息
                final int select = selector.select();
                // 遍历selector中所有selectedKeys
                for (SelectionKey key : selector.selectedKeys()) {
                    // 判断是否为连接建立的类型
                    if (key.isConnectable()) {
                        // 获取绑定的SocketChannel
                        SocketChannel channel = (SocketChannel)key.channel();
                        // 完成连接的建立（TCP/IP的三次握手）
                        channel.finishConnect();
                    }
                    if (key.isReadable()) {
                        // 获取绑定的SocketChannel
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        final int read = channel.read(byteBuffer);
                        if (read > 0) {
                            System.out.println("NIO 客户端：" + new String(byteBuffer.array(), 0, read));
                        }
                    }
                    if (key.isWritable()) {
                        // 获取绑定的SocketChannel
                        SocketChannel channel = (SocketChannel)key.channel();
                        channel.write(StandardCharsets.UTF_8.encode("你好，世界"));
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
