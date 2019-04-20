package com.tom;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class Producer {


    private KafkaProducer<Integer, String> producer = null;

    public Producer() {
        Properties props=new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Constants.KAFKA_BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"producerDemo");

        this.producer = new KafkaProducer<Integer, String>(props);
    }

    public static void main(String[] args) {
        Producer kafkaProducer = new Producer();
        kafkaProducer.sendMsg();
    }

    private void sendMsg() {
        producer.send(new ProducerRecord<Integer, String>(Constants.TOPIC, 1, "value"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.printf("Message send to ["+recordMetadata.partition()+"],offset ["+recordMetadata.offset()+"]");
            }
        });

    }

}