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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaListenerConfig {


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BOOTSTRAP_SERVER);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Bean
    public DefaultKafkaConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(100);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // build factory
    @Bean(QueueEvents.PUT_TRANSACTION)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> putTransactionContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(100);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean(QueueEvents.ACTIVATE_DORMANT)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> activateDormantContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(100);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // build listeners
    @KafkaListener(
            id = QueueEvents.PUT_TRANSACTION, topics = KafkaConstants.KAFKA_TOPIC,
            containerGroup = QueueEvents.PUT_TRANSACTION, containerFactory = QueueEvents.PUT_TRANSACTION
    )
    public void receivePutTransactionListener(String message) {
        System.out.println(message);
    }

    @KafkaListener(
            id = QueueEvents.ACTIVATE_DORMANT, topics = KafkaConstants.KAFKA_TOPIC,
            containerGroup = QueueEvents.ACTIVATE_DORMANT, containerFactory = QueueEvents.ACTIVATE_DORMANT
    )
    public void receiveActivateDormantListener(String message) {
        System.out.println(message);
    }
}
