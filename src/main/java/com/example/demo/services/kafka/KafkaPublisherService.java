package com.example.demo.services.kafka;

import com.example.demo.domains.kafka.Message;

public interface KafkaPublisherService {
    void sendMessage(String topic, Message message);
}
