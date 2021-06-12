package com.sample;

import com.sample.common.config.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

/**
 * This is class is used to initialize our Orders and Payments topics, which in production I assume it will be
 * pre-created with more configurations. Hence, the configuration that we don't need it on production env.
 */
@Configuration
@Profile("!prod")
public class StartupHousekeeper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public NewTopic createOrdersTopic() {

        return TopicBuilder.name(KafkaConstants.ORDERS_TOPIC_NAME).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic createPaymentsTopic() {

        return TopicBuilder.name(KafkaConstants.PAYMENTS_TOPIC_NAME).partitions(1).replicas(1).build();
    }
}
