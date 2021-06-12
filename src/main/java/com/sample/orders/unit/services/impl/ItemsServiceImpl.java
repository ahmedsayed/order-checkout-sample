package com.sample.orders.unit.services.impl;

import com.sample.common.domains.Item;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.exceptions.ResourceNotFoundException;
import com.sample.orders.repositories.ItemsRepository;
import com.sample.orders.unit.services.ItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemsServiceImpl implements ItemsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final ItemsRepository itemsRepository;
    
    @Autowired
    public ItemsServiceImpl(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public List<Item> findItems() {
        return this.itemsRepository.findAll();
    }

    @Override
    public Item findItem(long itemId) {
        Optional<Item> exitingItem = this.itemsRepository.findById(itemId);

        if (exitingItem.isPresent()) {
            return exitingItem.get();
        } else {
            throw new ResourceNotFoundException("Record not found with id: " + itemId);
        }
    }

    @Override
    public Item isValidItem(long itemId) throws BusinessException {
        try {
            Item item = findItem(itemId);

            // check for item validity (active, etc..)

            return item;
        } catch (ResourceNotFoundException ex) {
            log.info("no item found with id {}", itemId);
            throw new BusinessException("invalid-item", "The item Id is invalid");
        }
    }
}
