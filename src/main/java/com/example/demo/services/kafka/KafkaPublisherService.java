package com.example.demo.services.kafka;

import com.example.demo.domains.kafka.Message;
import org.springframework.stereotype.Service;

public interface KafkaPublisherService {
    public void sendMessage(String topic, Message message);
}
