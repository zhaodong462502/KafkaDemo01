package com.example.kafkademo01.kafka2;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyProducerInterceptor implements ProducerInterceptor {
    // 调用send方法会回调到这里.
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        System.out.println("MyProducerInterceptor-onSend ");
        return record;
    }

    // 当服务器返回数据会调用这里.
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        System.out.println("MyProducerInterceptor-onAcknowledgement");
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }


}

