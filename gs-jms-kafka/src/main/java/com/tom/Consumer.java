package com.tom;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class Consumer extends ShutdownableThread {
    private KafkaConsumer<Integer,String> consumer=null;
    public Consumer() {
        super("KafkaConsumerTest", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,Constants.KAFKA_BROKER_LIST);
        //GroupId所属的分组
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"DemoGroup1");
        //是否自动提交消息offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        //自动提交的间隔时间
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        //设置最开始offset的偏移量为当前offset的最早消息
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //设置心跳时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
        //设置key和value反序列化对象
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer=new  KafkaConsumer<Integer,String>(props);
    }

    @Override
    public void doWork() {
        this.consumer.subscribe(Collections.singletonList(Constants.TOPIC));
        ConsumerRecords<Integer, String> polls = this.consumer.poll(1000);
        for(ConsumerRecord record:polls){
            System.out.printf("partition ["+record.partition()+"],key=["+record.key()+"],value=["+record.value()+"],offset="+record.offset()+"");
        }
    }

    public static void main(String[] args) {
        Consumer kafkaConsumer = new Consumer();
        kafkaConsumer.start();
    }
}
