package com.sample.orders.web.rest;

import com.sample.common.domains.Order;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.unit.services.OrdersService;
import com.sample.common.domains.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkoutOrder(@Validated @RequestBody Order order) throws BusinessException {
        return ResponseEntity.ok().body(this.ordersService.checkoutOrder(order));
    }

    @GetMapping("/check-payment/{orderId}")
    public ResponseEntity<PaymentStatus> checkOrderPaymentStatus(@PathVariable Long orderId) throws BusinessException {
        return ResponseEntity.ok().body(this.ordersService.checkPaymentStatus(orderId));
    }
}
