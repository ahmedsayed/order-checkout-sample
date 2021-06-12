package com.sample.orders.unit.services;

import com.sample.common.domains.Order;
import com.sample.common.exceptions.BusinessException;
import com.sample.common.domains.PaymentStatus;

public interface OrdersService {

    Order checkoutOrder(Order order) throws BusinessException;

    Order saveOrder(Order order);

    void confirmOrderPayment(long orderId, PaymentStatus status);

    PaymentStatus checkPaymentStatus(Long orderId);
}
