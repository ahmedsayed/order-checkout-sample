package com.sample.common.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_items")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItem extends Auditable {

    @Id
    @GeneratedValue(generator = "orders_items_generator")
    @SequenceGenerator(name = "orders_items_generator", sequenceName = "orders_items_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        return new EqualsBuilder()
                .append(id, orderItem.id)
                .append(order, orderItem.order)
                .append(item, orderItem.item)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(order)
                .append(item)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + (order == null? "" : order.getId()) +
                ", item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
