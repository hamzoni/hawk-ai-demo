package com.example.demo.services.kafka;

import com.example.demo.constants.KafkaConstants;
import com.example.demo.constants.QueueEvents;
import com.example.demo.domains.kafka.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerService {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = QueueEvents.ACTIVATE_DORMANT
    )
    public void listenActivateDormant(Message message) {
        log.info("[Kafka Listener] Message received for group " + QueueEvents.ACTIVATE_DORMANT);
        template.convertAndSend(QueueEvents.ACTIVATE_DORMANT, message);
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = QueueEvents.PUT_TRANSACTION
    )
    public void listenPutTransaction(Message message) {
        log.info("[Kafka Listener] Message received for group " + QueueEvents.PUT_TRANSACTION);
        template.convertAndSend(QueueEvents.PUT_TRANSACTION, message);
    }
}
