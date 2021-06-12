package com.sample.orders.unit.services.impl;

import com.sample.common.config.constants.BusinessConstants;
import com.sample.common.config.constants.KafkaConstants;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.exceptions.ResourceNotFoundException;
import com.sample.orders.repositories.OrdersRepository;
import com.sample.common.domains.Order;
import com.sample.common.domains.OrderItem;
import com.sample.common.domains.Partner;
import com.sample.common.domains.User;
import com.sample.common.domains.PaymentStatus;
import com.sample.orders.unit.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Autowired
    private OrdersService self;

    private final PartnersService partnersService;
    private final UsersService usersService;
    private final OrderItemsService orderItemsService;
    private final FraudDetectionService fraudDetectionService;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository, PartnersService partnersService,
                             UsersService usersService, OrderItemsService orderItemsService,
                             FraudDetectionService fraudDetectionService, KafkaTemplate<String, Order> kafkaTemplate) {
        this.ordersRepository = ordersRepository;
        this.partnersService = partnersService;
        this.usersService = usersService;
        this.orderItemsService = orderItemsService;
        this.fraudDetectionService = fraudDetectionService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Order checkoutOrder(Order order) throws BusinessException {

        initOrder(order);

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new BusinessException("empty-order", "The order is empty and invalid.");
        }

        Partner seller = partnersService.isValidPartner(order.getSeller());
        order.setSeller(seller);

        User buyer = usersService.isValidUser(order.getBuyer());
        order.setBuyer(buyer);

        order.setTotalAmount(BigDecimal.ZERO);

        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem = orderItemsService.isValidOrderItem(orderItem, order.getSeller());

            orderItem.setTotalPrice(orderItem.getItem().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            order.setTotalAmount(order.getTotalAmount().add(orderItem.getTotalPrice()));
        }

        validateOrder(order);

        fraudDetectionService.validateOrder(order);

        // save the order in database in a separate transaction because I need the orderId in the kafka message
        order = self.saveOrder(order);

        kafkaTemplate.send(KafkaConstants.ORDERS_TOPIC_NAME, order.getId().toString(), order);

        return order;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Order saveOrder(Order order) {

        return ordersRepository.save(order);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void confirmOrderPayment(long orderId, PaymentStatus status) {

        Optional<Order> retrievedOrder = ordersRepository.findById(orderId);

        if (retrievedOrder.isPresent()) {
            Order order = retrievedOrder.get();

            if (!status.equals(order.getPaymentStatus())) {
                order.setPaymentStatus(status);

                ordersRepository.save(order);
            }
        }
    }

    @Override
    public PaymentStatus checkPaymentStatus(Long orderId) {

        Optional<Order> exitingOrder = this.ordersRepository.findById(orderId);

        if (exitingOrder.isPresent()) {
            return exitingOrder.get().getPaymentStatus();
        } else {
            throw new ResourceNotFoundException("Record not found with id: " + orderId);
        }
    }

    private void initOrder(Order order) {

        order.setOrderTime(LocalDateTime.now());
        order.setPaymentStatus(PaymentStatus.PROCESSING);
    }

    private void validateOrder(Order order) throws BusinessException {

        if (BusinessConstants.MIN_ORDER_TOTAL_AMOUNT.compareTo(order.getTotalAmount()) > 0) {
            throw new BusinessException("order-total-less-than-min",
                    String.format("The order total amount must be greater than %.2f", BusinessConstants.MIN_ORDER_TOTAL_AMOUNT));
        }
    }
}
