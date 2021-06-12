package com.sample.orders.listners;

import com.sample.orders.unit.services.OrdersService;
import com.sample.common.domains.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class PaymentKafkaListener {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final OrdersService ordersService;

    @Autowired
    public PaymentKafkaListener(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // initial offset should be stored so in the event of starting a service instance, it only continue from the last known new message in the topic
    @KafkaListener(id = "payments",
            topicPartitions = {
                @TopicPartition(topic = "payments", partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))
    })
    private void paymentListener(@Payload Payment payment) {
        
        log.info("listener received a new payment {}", payment.getId());

        ordersService.confirmOrderPayment(payment.getOrderId(), payment.getStatus());
    }
}
