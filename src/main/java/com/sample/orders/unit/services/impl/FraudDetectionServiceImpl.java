package com.sample.orders.unit.services.impl;

import com.sample.common.config.constants.BusinessConstants;
import com.sample.common.domains.Order;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.unit.services.FraudDetectionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FraudDetectionServiceImpl implements FraudDetectionService {

    @Override
    public void validateOrder(Order order) throws BusinessException {

        if(BusinessConstants.ORDER_TOTAL_AMOUNT_FRAUD_THRESHOLD.compareTo(order.getTotalAmount()) < 0) {
            throw new BusinessException("order-amount-exceed-fraud-limit",
                    String.format("The order total amount cannot exceed %.2f.", BusinessConstants.ORDER_TOTAL_AMOUNT_FRAUD_THRESHOLD));
        }
    }
}
