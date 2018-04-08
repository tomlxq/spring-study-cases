package com.example.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {

        //获取连接以及MQ通道
        Connection conn = ConnUtil.getConn();
        //从连接中获取创建通道
        Channel channel = conn.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = ConnUtil.getMessage(argv);

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        conn.close();
    }
    //...
}
