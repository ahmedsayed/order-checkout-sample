package com.sample.orders.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.common.domains.*;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.repositories.OrdersRepository;
import org.hamcrest.core.CombinableMatcher;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class OrdersIntegrationTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String API_URL_PREFIX = "/api/orders/";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "pass";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    public void checkoutOrder_IsNotAuthenticated_ThrowsException() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(API_URL_PREFIX + "checkout")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Order())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void checkoutOrder_ValidOrder_AddedSuccessfullyWithCorrectStatus() throws Exception {

        Partner requestedPartner = new Partner();
        requestedPartner.setId(1L);

        User requestedUser = new User();
        requestedUser.setId(1L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L, "dummy", BigDecimal.TEN, requestedPartner, true));
        requestedOrderItem.setQuantity(12);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        mvc.perform(MockMvcRequestBuilders.post(API_URL_PREFIX + "checkout")
                .with(user(USERNAME).password(PASSWORD))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestedOrder)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id",
                        CombinableMatcher.both(IsNull.notNullValue()).and(IsNot.not(0L)), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("paymentStatus",
                        CombinableMatcher.either(is(PaymentStatus.PROCESSING.toString()))
                                .or(is(PaymentStatus.SUCCESSFUL.toString()))
                                .or(is(PaymentStatus.FAILED.toString())), String.class));

        Optional<Order> o = ordersRepository.findById(1L);
        assertNotNull(o.get(), "order isn't saved in database");
    }

    @Test
    public void checkoutOrder_InvalidOrderWithAmountExceedsFraudThreshold_ThrowsException() throws Exception {

        Partner requestedPartner = new Partner();
        requestedPartner.setId(1L);

        User requestedUser = new User();
        requestedUser.setId(1L);

        ArrayList<OrderItem> requestedOrderItems = new ArrayList<>();
        OrderItem requestedOrderItem = new OrderItem();
        requestedOrderItem.setItem(new Item(6L, "dummy", null, requestedPartner, true));
        requestedOrderItem.setQuantity(50);
        requestedOrderItems.add(requestedOrderItem);

        Order requestedOrder = new Order();
        requestedOrder.setSeller(requestedPartner);
        requestedOrder.setBuyer(requestedUser);
        requestedOrder.setOrderItems(requestedOrderItems);

        mvc.perform(MockMvcRequestBuilders.post(API_URL_PREFIX + "checkout")
                .with(user(USERNAME).password(PASSWORD))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestedOrder)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof BusinessException);
                    assertEquals(((BusinessException)result.getResolvedException()).getCode(), "order-amount-exceed-fraud-limit");
                });

        Optional<Order> o = ordersRepository.findById(1L);
        assertFalse(o.isPresent(), "the order is saved in database");
    }
}
