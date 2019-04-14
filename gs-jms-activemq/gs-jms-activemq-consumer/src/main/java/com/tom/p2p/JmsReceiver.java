package com.tom.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsReceiver {
    public static void main(String[] args) {
        //ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("nio://192.168.238.150:61618?jms.optimizeAcknowledge=true");
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("failover:(tcp://192.168.238.150:61616?wireFormat.maxInactivityDuration=0,tcp://192.168.238.155:61616?wireFormat.maxInactivityDuration=0)");
        Connection connection = null;
        try {
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue("first-queue");
            MessageConsumer consumer = session.createConsumer(destination);
            for (int i = 0; i < 10; i++) {
                TextMessage receive = (TextMessage) consumer.receive();
                if (i == 5) {
                    receive.acknowledge();
                }
                System.out.println(receive.getText());
            }
            session.commit();
            session.close();
            System.out.println("接收消息完成");
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
