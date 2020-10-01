package com.example.demo.configs;

import com.example.demo.constants.KafkaConstants;
import com.example.demo.constants.QueueEvents;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.LogIfLevelEnabled;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaListenerConfig {

    public Map<String, Object> createConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        // required config
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BOOTSTRAP_SERVER);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaCustomDeserializer.class);

        // additional config
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return props;
    }

    private ConcurrentKafkaListenerContainerFactory<Object, Object> createConcurrentConsumerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        factory.setBatchListener(false);
        factory.setConcurrency(2);
        factory.setAutoStartup(false);

        // config container properties
        factory.getContainerProperties().setCommitLogLevel(LogIfLevelEnabled.Level.INFO);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setLogContainerConfig(true);

        factory.setErrorHandler(new SeekToCurrentErrorHandler());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> defaultContainerFactory() {
        return createConcurrentConsumerFactory();
    }

    // build factory
    @Bean(QueueEvents.PUT_TRANSACTION)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> putTransactionContainerFactory() {
        return createConcurrentConsumerFactory();
    }

    @Bean(QueueEvents.ACTIVATE_DORMANT)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> activateDormantContainerFactory() {
        return createConcurrentConsumerFactory();
    }

    // build listeners
    // one consumer per group, one group per topic -> one consumer per topic
    @KafkaListener(
            id = QueueEvents.PUT_TRANSACTION, topics = QueueEvents.PUT_TRANSACTION,
            containerGroup = QueueEvents.PUT_TRANSACTION, containerFactory = QueueEvents.PUT_TRANSACTION
    )
    public void receivePutTransactionListener(String message) {
        System.out.println(message);
    }

    @KafkaListener(
            id = QueueEvents.ACTIVATE_DORMANT, topics = QueueEvents.ACTIVATE_DORMANT,
            containerGroup = QueueEvents.ACTIVATE_DORMANT, containerFactory = QueueEvents.ACTIVATE_DORMANT
    )
    public void receiveActivateDormantListener(String message) {
        System.out.println(message);
    }
}
