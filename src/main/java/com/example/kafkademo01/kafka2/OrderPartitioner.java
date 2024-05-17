package com.example.kafkademo01.kafka2;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class OrderPartitioner implements Partitioner {

    // topic计数器. 每个topic都维护一个计数器. 这里可以考虑把map设置为安全的, 因为会出现并发问题.
    private HashMap<String, AtomicInteger> map = new HashMap<>();

    private static final Function<String, AtomicInteger> provider = s -> new AtomicInteger(0);

    // 这里业务逻辑其实不对,如果写入失败,那么永远也是
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> list = cluster.availablePartitionsForTopic(topic);
        AtomicInteger integer = map.computeIfAbsent(topic, provider);
        return integer.incrementAndGet() % list.size();
    }



    @Override
    public void close() {
        // 清空释放内存
        map.clear();
    }



    @Override
    public void configure(Map<String, ?> map) {

    }
}

