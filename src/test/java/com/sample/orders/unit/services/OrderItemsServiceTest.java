package com.sample.orders.unit.services;

import com.sample.common.config.constants.BusinessConstants;
import com.sample.common.domains.Item;
import com.sample.common.domains.OrderItem;
import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderItemsServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @MockBean
    private ItemsService itemsService;

    @Autowired
    private OrderItemsService orderItemsService;

    @Test
    public void isValidOrderItem_WithInvalidItem_ThrowsException() throws Exception {

        Long dummyId = 50L;

        OrderItem orderItem = new OrderItem();
        Item item = new Item(dummyId);
        orderItem.setItem(item);

        BDDMockito.given(itemsService.isValidItem(dummyId)).willThrow(new BusinessException("invalid-item", ""));

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, any(Partner.class)),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-item", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithNotAvailableItem_ThrowsException() throws Exception {

        Long dummyId = 50L;

        Item item = new Item(dummyId, "dummy", null, null, false);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(item);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, null),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("orderItem-item-not-available", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithWrongItemSeller_ThrowsException() throws Exception {

        Long correctPartnerId = 2L;
        Partner correctPartner = new Partner(correctPartnerId);

        Item item = new Item(1L, "dummy", null, correctPartner, true);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(item);

        Long requestedPartnerId = 50L;
        Partner requestedPartner = new Partner(requestedPartnerId);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, requestedPartner),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("orderItem-item-wrong-seller", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithZeroQuantity_ThrowsException() throws Exception {

        Partner partner = new Partner(50L);

        Item item = new Item(1L, "dummy", null, partner, true);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(0);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(item);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, partner),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-orderItem-quantity", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithNullQuantity_ThrowsException() throws Exception {

        Partner partner = new Partner(50L);

        Item item = new Item(1L, "dummy", null, partner, true);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(item);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, partner),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-orderItem-quantity", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithQuantityGreaterThanMaxAllowed_ThrowsException() throws Exception {

        Partner partner = new Partner(50L);

        Item item = new Item(1L, "dummy", null, partner, true);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(BusinessConstants.MAX_ITEM_QUANTITY_IN_ORDER + 1);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(item);

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> orderItemsService.isValidOrderItem(orderItem, partner),
                "Expected isValidOrderItem() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-orderItem-quantity", thrown.getCode());
    }

    @Test
    public void isValidOrderItem_WithValidOrderItem_ReturnsOrderItem() throws Exception {

        Partner partner = new Partner(50L);
        Item retrievedItem = new Item(1L, "dummy", BigDecimal.TEN, partner, true);

        BDDMockito.given(itemsService.isValidItem(anyLong())).willReturn(retrievedItem);

        Item requestedItem = new Item(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(requestedItem);
        orderItem.setQuantity(BusinessConstants.MIN_ITEM_QUANTITY_IN_ORDER);

        orderItem = orderItemsService.isValidOrderItem(orderItem, partner);

        assertNotNull(orderItem.getItem(), "expected valid item but returned null");
        assertNotNull(orderItem.getItem().getPrice(), "item price cannot be null");
        assertEquals(orderItem.getItem().getSeller(), partner, "wrong seller found");
    }
}
