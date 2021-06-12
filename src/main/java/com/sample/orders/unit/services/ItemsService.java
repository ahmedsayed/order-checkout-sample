package com.sample.orders.unit.services;

import com.sample.common.domains.Item;
import com.sample.common.exceptions.BusinessException;

import java.util.List;

public interface ItemsService {
    
    List<Item> findItems();

    Item findItem(long itemId);

    Item isValidItem(long itemId) throws BusinessException;
}
