package com.example.demo.services;

import com.example.demo.domains.kafka.Message;
import org.springframework.stereotype.Service;

@Service
public interface KafkaService {
    public void sendMessage(String topic, Message message);
}
