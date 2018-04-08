package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取连接以及MQ通道
        Connection conn = ConnUtil.getConn();
        //从连接中获取创建通道
        Channel channel = conn.createChannel();
        //声明创建队列队列

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for(int i=0;i<10;i++) {
            //消息内容
            String message = "Hello World!"+i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        //关闭通道和连接
        channel.close();
        conn.close();
    }
}
