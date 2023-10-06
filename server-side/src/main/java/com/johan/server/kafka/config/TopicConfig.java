package com.johan.server.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for defining Kafka topics.
 */
@Configuration
public class TopicConfig {

    /**
     * Creates and configures a new Kafka topic called "disturbance-reports."
     *
     * @return A NewTopic instance representing the Kafka topic.
     */
    @Bean
    public NewTopic reportsTopic() {
        return TopicBuilder
                .name("disturbance-reports")
                .replicas(3) // The number of replicas for fault tolerance.
                .partitions(3) // The number of partitions for parallelism.
                .build();
    }
}
