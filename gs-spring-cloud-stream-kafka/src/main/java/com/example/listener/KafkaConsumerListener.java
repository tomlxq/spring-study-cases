package com.example.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka 消费者监听
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@Component
public class KafkaConsumerListener {
    @KafkaListener(topics = "${kafka.topic}")
    public void onMessage(String message) {
        System.out.println("消费者监听消息：kafka.topic" + message);

    }

    @KafkaListener(topics = "${kafka.topic2}")
    public void onTomMessage(String message) {
        System.out.println("消费者监听消息 kafka.topic2 ：" + message);

    }
}
