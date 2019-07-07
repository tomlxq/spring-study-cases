package com.example;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GsSpringCloudStreamKafkaApplicationTests {

    @Test
    public void contextLoads() throws InterruptedException, ExecutionException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.238.165:9092");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        // 创建一个producer
        KafkaProducer<String, String> producer = new KafkaProducer(properties);
        // 创建消息
        String topic = "testMyTopic";
        Integer partition = 0;
        Long timestamp = System.currentTimeMillis();
        String key = "message";
        String value = "this is first message";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, timestamp, key, value);
        // 发送消息
        Future<RecordMetadata> send = producer.send(record);
        // 强制执行
        send.get();

    }

}
