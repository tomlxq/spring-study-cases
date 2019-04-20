package com.tom;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerBatch implements Runnable {


    private KafkaProducer<Integer, String> producer = null;

    public ProducerBatch() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.KAFKA_BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producerDemo");

        this.producer = new KafkaProducer<Integer, String>(props);
    }
    @Override
    public void run() {
        int messageNo = 0;
        while (true) {
            String msg = "message " + messageNo;
            producer.send(new ProducerRecord<Integer, String>(Constants.TOPIC, messageNo, msg), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.printf("Message send to [" + recordMetadata.partition() + "],offset [" + recordMetadata.offset() + "]");
                }
            });
            messageNo++;
        }
    }

    public static void main(String[] args) {
        ProducerBatch kafkaProducer = new ProducerBatch();
        new Thread(kafkaProducer).start();
    }




}