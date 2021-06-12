package com.sample.orders.unit.services;

import com.sample.common.domains.Order;
import com.sample.common.exceptions.BusinessException;

public interface FraudDetectionService {

    void validateOrder(Order order) throws BusinessException;
}
