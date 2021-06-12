package com.sample.orders.repositories;

import com.sample.common.domains.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnersRepository extends JpaRepository<Partner, Long> {
}
