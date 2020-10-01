package com.example.demo.services.kafka;

import com.example.demo.constants.QueueEvents;
import com.example.demo.constants.WsConstants;
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
            topics = QueueEvents.ACTIVATE_DORMANT,
            groupId = QueueEvents.ACTIVATE_DORMANT
    )
    public void listenActivateDormant(Message message) {
        var wsEndpoint = WsConstants.WS_ENDPOINT + "/" + QueueEvents.ACTIVATE_DORMANT;
        log.info("[Kafka Listener] Message received for group " + QueueEvents.ACTIVATE_DORMANT);

        // send to client
        log.info("Sending to endpoint " + wsEndpoint);
        template.convertAndSend(wsEndpoint, message);
    }

    @KafkaListener(
            topics = QueueEvents.PUT_TRANSACTION,
            groupId = QueueEvents.PUT_TRANSACTION
    )
    public void listenPutTransaction(Message message) {
        var wsEndpoint = WsConstants.WS_ENDPOINT + "/" + QueueEvents.PUT_TRANSACTION;
        log.info("[Kafka Listener] Message received for group " + QueueEvents.PUT_TRANSACTION);

        // send to client
        log.info("Sending to endpoint " + wsEndpoint);
        template.convertAndSend(wsEndpoint, message);
    }
}
