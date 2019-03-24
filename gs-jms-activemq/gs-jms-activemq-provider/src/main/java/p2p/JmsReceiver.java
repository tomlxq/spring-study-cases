package p2p;

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
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = null;
        try {
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue("first-queue");
            MessageConsumer consumer = session.createConsumer(destination);
            for (int i = 0; i < 10; i++) {
                TextMessage receive = (TextMessage) consumer.receive();
                System.out.println(receive.getText());
                if (i == 5) {
                    receive.acknowledge();
                }
            }
            // session.commit();
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
