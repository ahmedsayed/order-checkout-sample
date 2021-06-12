package com.sample.orders.unit.services;

import com.sample.common.config.constants.BusinessConstants;
import com.sample.common.domains.*;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.repositories.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String USERNAME = "user";

    @MockBean
    private OrdersRepository ordersRepository;

    @MockBean
    private PartnersService partnersService;

    @MockBean
    private UsersService usersService;

    @MockBean
    private OrderItemsService orderItemsService;

    @MockBean
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    private OrdersService ordersService;

    @Test
    public void checkoutOrder_EmptyOrder_ThrowsException() throws Exception {

        Order requestedOrder = new Order();

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("empty-order", thrown.getCode());
    }

    @Test
    public void checkoutOrder_OrderWithInvalidSeller_ThrowsException() throws Exception {

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        requestedOrderItems.add(new OrderItem());

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willThrow(new BusinessException("", "Invalid Partner"));

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("Invalid Partner", thrown.getMessage());
    }

    @Test
    public void checkoutOrder_OrderWithInvalidBuyer_ThrowsException() throws Exception {

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        requestedOrderItems.add(new OrderItem());

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willThrow(new BusinessException("", "Invalid User"));

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("Invalid User", thrown.getMessage());
    }
    
    @Test
    public void checkoutOrder_OrderWithItemFromWrongSeller_ThrowsException() throws Exception {

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L));
        requestedOrderItems.add(requestedOrderItem);

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willReturn(requestedUser);
        BDDMockito.given(orderItemsService.isValidOrderItem(any(OrderItem.class), any(Partner.class)))
                .willThrow(new BusinessException("orderItem-item-wrong-seller", ""));

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("orderItem-item-wrong-seller", thrown.getCode());
    }

    @Test
    public void checkoutOrder_OrderWithTotalAmountLessThanMin_ThrowsException() throws Exception {

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(
                new Item(6L, "dummy", BusinessConstants.MIN_ORDER_TOTAL_AMOUNT.subtract(BigDecimal.ONE), requestedPartner, true));
        requestedOrderItem.setQuantity(1);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willReturn(requestedUser);
        BDDMockito.given(orderItemsService.isValidOrderItem(any(OrderItem.class), any(Partner.class))).willReturn(requestedOrderItem);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("order-total-less-than-min", thrown.getCode());
    }
    
    @Test
    public void checkoutOrder_OrderWithTotalAmountGreaterThanMax_ThrowsException() throws Exception {

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L, "dummy", BigDecimal.valueOf(200), requestedPartner, true));
        requestedOrderItem.setQuantity(10);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willReturn(requestedUser);
        BDDMockito.given(orderItemsService.isValidOrderItem(any(OrderItem.class), any(Partner.class))).willReturn(requestedOrderItem);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("order-amount-exceed-fraud-limit", thrown.getCode());
    }
    
    @Test
    public void checkoutOrder_OrderWithItemWithZeroQuantity_ThrowsException() throws Exception {
        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L, "dummy", BigDecimal.valueOf(200), requestedPartner, true));
        requestedOrderItem.setQuantity(0);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willReturn(requestedUser);
        BDDMockito.given(orderItemsService.isValidOrderItem(any(OrderItem.class), any(Partner.class))).willReturn(requestedOrderItem);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> ordersService.checkoutOrder(requestedOrder),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("order-total-less-than-min", thrown.getCode());
    }

    @Test
    public void checkoutOrder_ValidOrder_SavedSuccessfullyAndReturnsOrder() throws Exception {

        Partner requestedPartner = new Partner();
        requestedPartner.setId(50L);

        User requestedUser = new User();
        requestedUser.setId(50L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L, "dummy", BigDecimal.TEN, requestedPartner, true));
        requestedOrderItem.setQuantity(10);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        BDDMockito.given(partnersService.isValidPartner(any(Partner.class))).willReturn(requestedPartner);
        BDDMockito.given(usersService.isValidUser(any(User.class))).willReturn(requestedUser);
        BDDMockito.given(orderItemsService.isValidOrderItem(any(OrderItem.class), any(Partner.class))).willReturn(requestedOrderItem);

        when(ordersRepository.save(any(Order.class))).thenAnswer((Answer<Order>) invocation -> {
            Object[] args = invocation.getArguments();
            Order o = ((Order) args[0]);
            o.setId(1L);
            return o;
        });

        Order result = ordersService.checkoutOrder(requestedOrder);

        assertNotNull(result.getId(), "order isn't saved");
        assertNotNull(result.getPaymentStatus(), "order status shouldn't be empty");
        assertThat(result.getPaymentStatus()).as("order status is wrong")
                .isIn(PaymentStatus.PROCESSING, PaymentStatus.SUCCESSFUL, PaymentStatus.FAILED);
    }
}
