package com.example.LogConsumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/*
@Component
public class LogConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogConsumer.class);

    @JmsListener(destination = QueueName.LOG_QUEUE)
    public void receivedQueue(String msg) {
        LOGGER.spring("Has received from " + QueueName.LOG_QUEUE + ", msg: " + msg);
    }
}*/
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "sample.queue")
    public void receiveQueue(String text) {
        System.out.println(text);
    }

}