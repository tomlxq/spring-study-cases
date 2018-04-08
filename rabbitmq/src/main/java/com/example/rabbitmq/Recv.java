package com.example.rabbitmq;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Recv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接以及MQ通道
        Connection conn = ConnUtil.getConn();
        //从连接中获取创建通道
        Channel channel = conn.createChannel();
        //声明创建队列队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        //定义队列的消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        ////监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


    }


}
