package com.sample.orders.integration;

import com.sample.common.domains.Item;
import com.sample.orders.unit.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemsController {

    private final ItemsService itemsService;

    @Autowired
    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> findAll() {
        return ResponseEntity.ok().body(itemsService.findItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findItem(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok().body(itemsService.findItem(id));
    }
}
