package com.example.stream.consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@EnableBinding(Sink.class)
public class MessageConsumerBean {
    /**
     * Sink.INPUT 为Bean的名称
     */
    @Autowired
    @Qualifier(Sink.INPUT)
    private SubscribableChannel subscribableChannel;

    /**
     * 当字段注入完成后的回调
     */
    @PostConstruct
    public void init() {
        subscribableChannel.subscribe(new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("subscribableChannel: " + message.getPayload());
            }
        });
    }

    /**
     * 通过@ServiceActivator订阅消息
     * 
     * @param message
     */
    @ServiceActivator(inputChannel = Sink.INPUT)
    public void onMessage(Object message) {
        System.out.println("ServiceActivator: " + message);
    }

    /**
     * 通过@ServiceActivator订阅消息
     *
     * @param message
     */
    @StreamListener(Sink.INPUT)
    public void onMessage2(String message) {
        System.out.println("StreamListener: " + message);
    }
}
