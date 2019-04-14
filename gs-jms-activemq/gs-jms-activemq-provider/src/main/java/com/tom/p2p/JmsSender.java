package com.tom.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsSender {
    public static void main(String[] args) {
        //ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("nio://192.168.238.150:61618?jms.optimizeAcknowledge=true");
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                "failover:(tcp://192.168.238.150:61616?wireFormat.maxInactivityDuration=0,tcp://192.168.238.155:61616?wireFormat.maxInactivityDuration=0)"
        );
        Connection connection = null;
        try {
            connection = activeMQConnectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("first-queue?customer.prefetchSize=100");

            MessageProducer producer = session.createProducer(destination);
            for (int i = 0; i < 10; i++) {
                TextMessage textMessage = session.createTextMessage("hello,world" + i);

                producer.send(textMessage);

            }
            session.commit();
            session.close();
            System.out.println("发送消息完成");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
