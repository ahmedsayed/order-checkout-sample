package com.sample.payments.services;

import com.sample.common.domains.Order;
import com.sample.common.domains.Payment;

public interface PaymentsService {

    Payment createPayment(Order order);

    void finalisePayment(Long paymentId);
}
