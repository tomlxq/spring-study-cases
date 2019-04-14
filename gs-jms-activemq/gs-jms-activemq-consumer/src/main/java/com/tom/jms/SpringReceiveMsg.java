package com.tom.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;

public class SpringReceiveMsg {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/service-jms.xml");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        String msg = (String) jmsTemplate.receiveAndConvert();
        System.out.println("接收消息完成：" + msg);
    }

}
