package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stream.producer.MessageProducerBean;

/**
 * {@link RabbitMQProducerController} 消息生产者
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@RestController
public class RabbitMQProducerController {

    public final String topic;
    public final MessageProducerBean producerMessageBean;

    @Autowired
    public RabbitMQProducerController(MessageProducerBean producerMessageBean,
        @Value("${rabbitmq.topic}") String topic) {

        this.topic = topic;
        this.producerMessageBean = producerMessageBean;
    }

    /**
     * 通过{@link MessageProducerBean}发送
     * 
     * @param message
     * @return
     */
    @PostMapping("/message/send2")
    public Boolean sendMsg2(@RequestParam String message) {
        producerMessageBean.send(message);
        return true;
    }
}
