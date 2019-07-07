package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.stream.producer.MessageProducerBean;

/**
 * {@link KafkaProducerController} 消息生产者
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@RestController
public class KafkaProducerController {
    public final KafkaTemplate kafkaTemplate;
    public final String topic;
    public final MessageProducerBean producerMessageBean;

    @Autowired
    public KafkaProducerController(KafkaTemplate kafkaTemplate, MessageProducerBean producerMessageBean,
        @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.producerMessageBean = producerMessageBean;
    }

    /**
     * 通过{@link KafkaTemplate}发送
     * 
     * @param message
     * @return
     */
    @PostMapping("/message/send")
    public Boolean sendMsg(@RequestParam String message) {
        ListenableFuture send = kafkaTemplate.send(this.topic, message);
        return true;
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
