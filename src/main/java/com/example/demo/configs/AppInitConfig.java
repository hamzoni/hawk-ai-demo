package com.example.demo.configs;

import com.example.demo.DemoApplication;
import com.example.demo.constants.KafkaConstants;
import com.example.demo.constants.QueueEvents;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AppInitConfig extends SpringBootServletInitializer {
    @SuppressWarnings("resource")
    public static void main(final String[] args) {
        var context = SpringApplication.run(DemoApplication.class, args);
//        context.getBean(Table.class).fillWithTestdata();

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BOOTSTRAP_SERVER);

        // https://stackoverflow.com/questions/51274085/how-do-you-programmatically-create-a-topic-with-kafka-1-1-0
        try (final var adminClient = AdminClient.create(props)) {
            var topics = Arrays.asList(
                    new NewTopic(QueueEvents.ACTIVATE_DORMANT, 1, (short) 1),
                    new NewTopic(QueueEvents.PUT_TRANSACTION, 1, (short) 1)
            );
            adminClient.createTopics(topics);
        }
    }
}
