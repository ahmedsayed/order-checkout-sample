package com.sample.common.config.kafka;

import com.sample.common.domains.Order;
import com.sample.common.domains.Payment;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    Map<String, Object> configProps;

    @PostConstruct
    private void postConstruct() {
        configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    }

    @Bean
    public ProducerFactory<String, Order> orderProducerFactory() {

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Order> orderKafkaTemplate() {

        return new KafkaTemplate<>(orderProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Payment> paymentProducerFactory() {

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Payment> paymentKafkaTemplate() {

        return new KafkaTemplate<>(paymentProducerFactory());
    }
}
