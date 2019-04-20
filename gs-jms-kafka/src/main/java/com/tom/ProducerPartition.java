package com.tom;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerPartition {


    private KafkaProducer<Integer, String> producer = null;

    public ProducerPartition() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.KAFKA_BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producerDemo");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.tom.MyPartition");
        this.producer = new KafkaProducer<Integer, String>(props);
    }

    public static void main(String[] args) {
        ProducerPartition kafkaProducer = new ProducerPartition();
        kafkaProducer.sendMsg();
    }

    private void sendMsg() {
        producer.send(new ProducerRecord<Integer, String>(Constants.TOPIC, 1, "value"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.printf("Message send to [" + recordMetadata.partition() + "],offset [" + recordMetadata.offset() + "]");
            }
        });
        System.out.println("发送完成");
    }

}