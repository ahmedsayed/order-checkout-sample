package com.sample.common.domains;

import com.sample.common.config.constants.JpaConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order  extends Auditable {

    @Id
    @GeneratedValue(generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Partner seller;

    @Column(name = "order_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
    private LocalDateTime orderTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Partner getSeller() {
        return seller;
    }

    public void setSeller(Partner seller) {
        this.seller = seller;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(id, order.id)
                .append(buyer, order.buyer)
                .append(seller, order.seller)
                .append(orderTime, order.orderTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(buyer)
                .append(seller)
                .append(orderTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyer=" + buyer +
                ", seller=" + seller +
                ", orderTime=" + orderTime +
                ", orderItems=" + orderItems +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
