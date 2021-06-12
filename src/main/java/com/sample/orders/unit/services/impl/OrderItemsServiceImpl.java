package com.sample.orders.unit.services.impl;

import com.sample.common.config.constants.BusinessConstants;
import com.sample.common.domains.Item;
import com.sample.common.domains.OrderItem;
import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.unit.services.ItemsService;
import com.sample.orders.unit.services.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderItemsServiceImpl implements OrderItemsService {

    private final ItemsService itemsService;

    @Autowired
    public OrderItemsServiceImpl(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @Override
    public OrderItem isValidOrderItem(OrderItem orderItem, Partner partner) throws BusinessException {

        Item item = itemsService.isValidItem(orderItem.getItem().getId());

        if (!item.isAvailable()) {
            throw new BusinessException("orderItem-item-not-available",
                    String.format("The item %s isn't available", item.getName()));
        }

        if (!item.getSeller().equals(partner)) {
            throw new BusinessException("orderItem-item-wrong-seller",
                    String.format("The item %s isn't available from this seller", item.getName()));
        }

        if (orderItem.getQuantity() == null
                || orderItem.getQuantity() < BusinessConstants.MIN_ITEM_QUANTITY_IN_ORDER
                || orderItem.getQuantity() > BusinessConstants.MAX_ITEM_QUANTITY_IN_ORDER) {
            throw new BusinessException("invalid-orderItem-quantity",
                    String.format("The quantity of %s isn't valid", item.getName()));
        }

        orderItem.setItem(item);

        // check for order item validity (active, etc..)

        return orderItem;
    }
}
