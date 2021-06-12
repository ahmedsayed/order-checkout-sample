package com.sample.payments.repositories;

import com.sample.common.domains.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
