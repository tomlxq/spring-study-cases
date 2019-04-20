package com.tom;

import kafka.utils.Implicits;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class KafkaProducer {


    private  org.apache.kafka.clients.producer.KafkaProducer<Integer,String> producer=null;
    public KafkaProducer(){
        Properties props=new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Constants.KAFKA_BROKER_LIST);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"producerDemo");

        this.producer=new org.apache.kafka.clients.producer.KafkaProducer<Integer,String>(props);
    }

    public static void main(String[] args) {
        KafkaProducer kafkaProducer = new KafkaProducer();
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