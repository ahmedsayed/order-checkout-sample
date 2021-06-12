package com.sample.orders.repositories;

import com.sample.common.domains.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    
}
