package com.tom.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsTopicSender {
    public static void main(String[] args) {
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.238.105:61616");
        Connection connection = null;
        try {
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("first-topic");
            MessageProducer producer = session.createProducer(destination);

            TextMessage textMessage = session.createTextMessage("hello,this is first topic");
            producer.send(textMessage);

            session.commit();
            session.close();
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
