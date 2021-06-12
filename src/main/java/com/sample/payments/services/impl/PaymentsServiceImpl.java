package com.sample.payments.services.impl;

import com.sample.common.config.constants.KafkaConstants;
import com.sample.common.domains.Order;
import com.sample.common.domains.Payment;
import com.sample.common.domains.PaymentStatus;
import com.sample.payments.repositories.PaymentsRepository;
import com.sample.payments.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final KafkaTemplate<String, Payment> paymentKafkaProducer;

    @Autowired
    public PaymentsServiceImpl(PaymentsRepository paymentsRepository, KafkaTemplate<String, Payment> paymentKafkaProducer) {
        this.paymentsRepository = paymentsRepository;
        this.paymentKafkaProducer = paymentKafkaProducer;
    }

    @Override
    public Payment createPayment(Order order) {

        Optional<Payment> retrievedPayment = paymentsRepository.findByOrderId(order.getId());
        Payment payment;

        if (retrievedPayment.isPresent()) {
            payment = retrievedPayment.get();
        } else {
            payment = new Payment();

            payment.setOrderId(order.getId());
            payment.setUserId(order.getBuyer().getId());
            payment.setAmount(order.getTotalAmount());
            payment.setStatus(PaymentStatus.NEW);

            paymentsRepository.save(payment);
        }
        return payment;
    }

    // TODO Dummy Implementation
    @Override
    public void finalisePayment(Long paymentId) {

        Optional<Payment> retrievedPayment = paymentsRepository.findById(paymentId);

        if (retrievedPayment.isPresent()) {

            Payment payment = retrievedPayment.get();

            // if new request, set start processing time
            if (payment.getStartTime() == null)
                payment.setStartTime(LocalDateTime.now());

            // if not finished, process the payment request
            if (payment.getFinishTime() == null) {
                double randomResult = Math.random();

                // random result: 70% success, 10% fail, 20% error and should try again
                if (randomResult <= 0.7) {
                    payment.setFinishTime(LocalDateTime.now());
                    payment.setStatus(PaymentStatus.SUCCESSFUL);
                } else if (randomResult <= 0.8) {
                    payment.setFinishTime(LocalDateTime.now());
                    payment.setStatus(PaymentStatus.FAILED);
                } else {
                    payment.setStatus(PaymentStatus.PROCESSING);
                }

                payment = paymentsRepository.save(payment);

                if (payment.getFinishTime() != null)
                    paymentKafkaProducer.send(KafkaConstants.PAYMENTS_TOPIC_NAME, payment.getId().toString(), payment);
            }
        }
    }
}
