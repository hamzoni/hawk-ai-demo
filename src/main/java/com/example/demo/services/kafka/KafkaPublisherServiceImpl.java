package com.example.demo.services.kafka;

import com.example.demo.constants.KafkaConstants;
import com.example.demo.domains.kafka.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaPublisherServiceImpl implements KafkaPublisherService {
    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Override
    public void sendMessage(String topic, Message message) {
        message.setTimestamp(LocalDateTime.now().toString());
        try {
            //Sending the message to kafka topic queue
            if (message.toString().isEmpty()) {
                log.error("Message is null, unable to send to Kafka");
            } else {
                kafkaTemplate.send(topic, message).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
