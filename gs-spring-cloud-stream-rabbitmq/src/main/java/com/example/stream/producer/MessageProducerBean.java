package com.example.stream.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.example.stream.message.TomMessageSource;

/**
 * 消息发送者
 *
 * @author TomLuo
 * @date 2019/7/6
 */
@EnableBinding({Source.class, TomMessageSource.class})
public class MessageProducerBean {
    @Autowired
    @Qualifier(Source.OUTPUT)
    MessageChannel messageChannel;
    @Autowired
    Source source;

    @Autowired
    TomMessageSource tomMessageSource;

    @Autowired
    @Qualifier(TomMessageSource.OUTPUT)
    private MessageChannel tomOutputMessageChannel;

    /**
     * 发送消息
     * 
     * @param message
     */
    public void send(String message) {
        messageChannel.send(MessageBuilder.withPayload("[messageChannel] send :" + message).build());
        source.output().send(MessageBuilder.withPayload("[source] send :" + message).build());
        tomOutputMessageChannel.send(MessageBuilder.withPayload("[tomOutputMessageChannel] send :" + message).build());
    }
}
