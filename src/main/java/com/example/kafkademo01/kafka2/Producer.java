package com.example.kafkademo01.kafka2;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;


public class Producer {

    public static void main(String[] args) {
        final String url = "192.168.124.149:9092";
        final String topic = "topic-0517-02";
        // 配置.
        HashMap<String, Object> config = new HashMap<>();

        // 连接地址
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);

        // ACK
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        // 相应超时.
        config.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 5000);

        // 缓冲区大小. (发送给服务器的)
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024 * 1024 * 10);
        // 每次最多发10K
        config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1024 * 10);
        // 不重试,有些非幂等性可以.
        config.put(ProducerConfig.RETRIES_CONFIG, 0);

        // snappy 压缩..
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // 序列化
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        config.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, OrderPartitioner.class);

        config.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Collections.singletonList(MyProducerInterceptor.class));


        // ok了.
        KafkaProducer<String, String> producer = new KafkaProducer<>(config);
        IntStream.range(0, 10).forEach(value -> {
            // 发送
            try {
                RecordMetadata future = producer.send(new ProducerRecord<>(topic, "cur-time", String.format("id: %d, time : %d.", value, System.currentTimeMillis())), (metadata, exception) -> {

                }).get();
                System.out.println("offset : "+future.offset()+", partition : "+future.partition());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        // 最后记得刷新出去.
        producer.flush();
    }
}

