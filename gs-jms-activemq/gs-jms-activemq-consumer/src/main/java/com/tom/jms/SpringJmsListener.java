package com.tom.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class SpringJmsListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("非阻塞接收消息：" + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
