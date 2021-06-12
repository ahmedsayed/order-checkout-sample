package com.sample.orders.unit.services;

import com.sample.common.domains.OrderItem;
import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;

public interface OrderItemsService {

    OrderItem isValidOrderItem(OrderItem orderItem, Partner partner) throws BusinessException;
}
