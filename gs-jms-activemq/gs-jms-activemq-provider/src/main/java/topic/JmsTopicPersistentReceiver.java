package topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class JmsTopicPersistentReceiver {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.238.105:61616");
                Connection connection = null;
                try {
                    connection = activeMQConnectionFactory.createConnection();
                    String clientId = "client_order" + Thread.currentThread().getName();
                    connection.setClientID(clientId);
                    connection.start();

                    Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
                    Topic topic = session.createTopic("first-topic");
                    MessageConsumer consumer = session.createDurableSubscriber(topic, clientId);
                    TextMessage receive = (TextMessage) consumer.receive();
                    System.out.println(receive.getText() + Thread.currentThread().getName());
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
            }).start();
        }
    }
}
