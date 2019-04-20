package com.tom;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //获取所有的分区数量
        Integer integer = cluster.partitionCountForTopic(topic);
        //获取key的hasCode
        int hashCode = key.hashCode();
        //默认算法
        return Math.abs(hashCode % integer.intValue());
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
