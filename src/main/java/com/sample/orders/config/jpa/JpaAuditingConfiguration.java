package com.sample.orders.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // get username from security context and set it in createdBy and lastModifiedBy fields
        return () -> {
            if(SecurityContextHolder.getContext().getAuthentication() == null)
                return Optional.of("SYSTEM");
            else
                return Optional.of(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName()).orElse("SYSTEM"));
        };
    }
}
