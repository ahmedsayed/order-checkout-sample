package com.sample.common.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sample.common.config.constants.JpaConstants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {

    @Id
    @GeneratedValue(generator = "payment_generator")
    @SequenceGenerator(name = "payment_generator", sequenceName = "payment_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
    private LocalDateTime startTime;

    @Column(name = "finish_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JpaConstants.DATA_TIME_FORMAT)
    private LocalDateTime finishTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
