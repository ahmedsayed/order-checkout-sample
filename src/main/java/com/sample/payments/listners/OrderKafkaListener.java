package com.sample.payments.listners;

import com.sample.common.domains.Order;
import com.sample.common.domains.Payment;
import com.sample.payments.services.PaymentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class OrderKafkaListener {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final PaymentsService paymentsService;

    @Autowired
    public OrderKafkaListener(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    // initial offset should be stored so in the event of starting a service instance, it only continue from the last known new message in the topic
    @KafkaListener(id = "orders",
            topicPartitions = {
                @TopicPartition(topic = "orders", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))
    })
    private void ordersListener(@Payload Order order) {
        
        log.info("listener received a new order {}", order.getId());

        Payment payment = paymentsService.createPayment(order);
        paymentsService.finalisePayment(payment.getId());
    }
}
